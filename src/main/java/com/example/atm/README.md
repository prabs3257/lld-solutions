# ATM LLD

Architecture:
- State classes: flow management only.
- State handlers: execute the action for the current state.
- Services: business logic.
- ATMContext: current state + session.
- ATMWorkflowEngine: orchestration.

Flow:
Idle -> HasCard -> Withdrawal/Deposit/CheckBalance -> Idle
