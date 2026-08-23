package com.example.loan;

import com.example.loan.enums.PaymentMode;
import com.example.loan.lock.LoanLockManager;
import com.example.loan.model.Installment;
import com.example.loan.model.LoanAccount;
import com.example.loan.model.SettlementQuote;
import com.example.loan.repository.InMemoryLoanRepository;
import com.example.loan.repository.LoanRepository;
import com.example.loan.service.LoanClosureService;
import com.example.loan.service.LoanService;
import com.example.loan.service.OverdueProcessingService;
import com.example.loan.service.RepaymentService;
import com.example.loan.strategy.ChargesInterestPrincipalStrategy;
import com.example.loan.strategy.EmiCalculator;
import com.example.loan.strategy.MonthlyEmiScheduleStrategy;
import com.example.loan.strategy.SimpleLoanClosureStrategy;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        LoanRepository loanRepository = new InMemoryLoanRepository();
        LoanLockManager lockManager = new LoanLockManager();

        LoanService loanService = new LoanService(
                loanRepository,
                new MonthlyEmiScheduleStrategy(new EmiCalculator())
        );

        RepaymentService repaymentService = new RepaymentService(
                loanRepository,
                new ChargesInterestPrincipalStrategy(),
                lockManager
        );

        OverdueProcessingService overdueService =
                new OverdueProcessingService(
                        loanRepository,
                        lockManager,
                        new BigDecimal("500")
                );

        LoanClosureService closureService =
                new LoanClosureService(
                        loanRepository,
                        new SimpleLoanClosureStrategy(),
                        lockManager
                );

        LoanAccount loan = loanService.createAndActivateLoan(
                "LOAN-1",
                "CUSTOMER-1",
                new BigDecimal("100000"),
                new BigDecimal("0.12"),
                LocalDate.now().minusMonths(1),
                12
        );

        System.out.println("===== INITIAL LOAN =====");
        printLoan(loan);

        System.out.println("\n===== PARTIAL PAYMENT =====");
        repaymentService.makeRepayment(
                "LOAN-1",
                new BigDecimal("3000"),
                PaymentMode.UPI,
                "PAYMENT-1"
        );
        printLoan(loanRepository.findById("LOAN-1").orElseThrow());

        System.out.println("\n===== OVERDUE PROCESSING =====");
        overdueService.processLoan(
                "LOAN-1",
                LocalDate.now().plusDays(5)
        );
        printLoan(loanRepository.findById("LOAN-1").orElseThrow());

        System.out.println("\n===== LARGE PAYMENT =====");
        repaymentService.makeRepayment(
                "LOAN-1",
                new BigDecimal("25000"),
                PaymentMode.BANK_TRANSFER,
                "PAYMENT-2"
        );
        printLoan(loanRepository.findById("LOAN-1").orElseThrow());

        System.out.println("\n===== DUPLICATE PAYMENT =====");
        repaymentService.makeRepayment(
                "LOAN-1",
                new BigDecimal("25000"),
                PaymentMode.BANK_TRANSFER,
                "PAYMENT-2"
        );

        System.out.println("\n===== EARLY CLOSURE =====");

        SettlementQuote quote = closureService.getSettlementQuote(
                "LOAN-1",
                LocalDate.now()
        );

        System.out.println("Settlement quote: " + quote);

        closureService.closeLoan(
                "LOAN-1",
                quote.totalSettlementAmount(),
                LocalDate.now()
        );

        printLoan(loanRepository.findById("LOAN-1").orElseThrow());
    }

    private static void printLoan(LoanAccount loan) {

        System.out.println("------------------------------");
        System.out.println("Loan ID: " + loan.getLoanId());
        System.out.println("Status: " + loan.getStatus());
        System.out.println("Outstanding Principal: "
                + loan.getOutstandingPrincipal());

        for (Installment installment : loan.getInstallments()) {
            System.out.println(
                    "Installment " + installment.getInstallmentNumber()
                            + " | Due=" + installment.getDueDate()
                            + " | Principal Remaining="
                            + installment.getRemainingPrincipal()
                            + " | Interest Remaining="
                            + installment.getRemainingInterest()
                            + " | Charges Remaining="
                            + installment.getRemainingCharges()
                            + " | Status="
                            + installment.getStatus()
            );
        }

        System.out.println("------------------------------");
    }
}
