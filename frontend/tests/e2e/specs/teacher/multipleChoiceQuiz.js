describe('Teacher Consult Multiple Choice Question Walkthrough', () => {
    before(() => {
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
      cy.get('[data-cy="endQuizButton"]').click();
      cy.get('[data-cy="confirmationButton"]').click();
      cy.wait(500);
      cy.logout();
    });
    beforeEach(() => {
      cy.server()
      cy.route('POST', '/quizzes/*/answers').as('answers');
    });
  
    afterEach(() => {
      cy.logout();
    });
  
    it('teacher consult quiz result', function () {
      cy.demoTeacherLogin();

      cy.get('[data-cy="managementMenuButton"]').click();
      cy.get('[data-cy="quizzesTeacherMenuButton"]').click();

      cy.get('tbody tr')
        .first()
        .within(($list) => {
          cy.get('[data-cy="table"]').click();
      });

      cy.wait(200)
    });
  });