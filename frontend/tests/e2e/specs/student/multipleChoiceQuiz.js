describe('Answer and consult Multiple Choice Question Walkthrough', () => {
  beforeEach(() => {
    cy.server()
    cy.route('GET', 'executions/*/quizzes/solved').as('solved');
    cy.route('POST', '/quizzes/*/conclude').as('concludeQuiz');
  });

  afterEach(() => {
    cy.logout();
  });

  it('Solve the quiz with empty answer', function () {
    //create quiz
    cy.demoTeacherLogin();
    cy.createQuestion(
      'Question Title',
      'Question',
      'Option',
      'Option',
      'Option',
      'Correct'
    );
    cy.createMultipleChoiceQuestion(
      'Multiple Correct Question Title',
      'Multiple Correct Question',
      'Correct - 1',
      'Option',
      'Option',
      'Correct - 2'
    );
    cy.createOrderedMultipleChoiceQuestion(
      'Ordered Question Title',
      'Ordered Question',
      'Correct - 1',
      'Correct - 2',
      'Option',
      'Option'
    );
    cy.createQuizzWith3Questions(
      'Multiple Choice Quiz Title',
      'Question Title',
      'Multiple Correct Question Title',
      'Ordered Question Title'
    );
    cy.contains('Logout').click();

    cy.demoStudentLogin();
    cy.get('[data-cy="quizzesStudentMenuButton"]').click();

    cy.contains('Available').click();
    cy.contains('Multiple Choice Quiz Title').click();

    cy.get('[data-cy="nextQuestionButton"]').click();
    cy.get('[data-cy="nextQuestionButton"]').click();
    cy.get('[data-cy="endQuizButton"]').click();
    cy.get('[data-cy="confirmationButton"]').click();

    cy.wait('@concludeQuiz').its('status').should('eq', 200);
    cy.wait(200)
    cy.get('[data-cy="nextQuestionButton"]').click();
    cy.get('[data-cy="nextQuestionButton"]').click();
  });

  it('Consult the quiz result', function () {
    cy.demoStudentLogin();
    cy.get('[data-cy="quizzesStudentMenuButton"]').click();

    cy.contains('Solved').click();
    cy.wait('@solved').its('status').should('eq', 200);
    cy.contains('Multiple Choice Quiz Title').click();
    cy.get('[data-cy="nextQuestionButton"]').click();
    cy.get('[data-cy="nextQuestionButton"]').click();
  });
});