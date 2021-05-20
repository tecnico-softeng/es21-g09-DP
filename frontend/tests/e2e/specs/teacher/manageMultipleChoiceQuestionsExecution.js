describe('Manage Multiple Choice Questions Walk-through', () => {
  function validateQuestion(
    title,
    content,
    optionPrefix = 'Option ',
    correctIndex = 2
  ) {
    cy.get('[data-cy="showQuestionDialog"]')
      .should('be.visible')
      .within(($ls) => {
        cy.get('.headline').should('contain', title);
        cy.get('span > p').should('contain', content);
        cy.get('li').each(($el, index, $list) => {
          cy.get($el).should('contain', optionPrefix + index);
          if (index === correctIndex) {
            cy.get($el).should('contain', '[★]');
          } else {
            cy.get($el).should('not.contain', '[★]');
          }
        });
      });
  }

  function validateQuestionFull(
    title,
    content,
    optionPrefix = 'Option ',
    correctIndex = 2
  ) {
    cy.log('Validate question with show dialog. ' + correctIndex);

    cy.get('[data-cy="questionTitleGrid"]').first().click();

    validateQuestion(title, content, optionPrefix, correctIndex);

    cy.get('button').contains('close').click();
  }

  before(() => {
    cy.cleanMultipleChoiceQuestionsByName('Cypress Question Example');
    cy.cleanCodeFillInQuestionsByName('Cypress Question Example');
  });
  after(() => {
    cy.cleanMultipleChoiceQuestionsByName('Cypress Question Example');
  });

  beforeEach(() => {
    cy.demoTeacherLogin();
    cy.server();
    cy.route('GET', '/courses/*/questions').as('getQuestions');
    cy.route('GET', '/courses/*/topics').as('getTopics');
    cy.get('[data-cy="managementMenuButton"]').click();
    cy.get('[data-cy="questionsTeacherMenuButton"]').click();

    cy.wait('@getQuestions').its('status').should('eq', 200);

    cy.wait('@getTopics').its('status').should('eq', 200);
  });

  afterEach(() => {
    cy.logout();
  });

  it('Creates a new multiple choice question', function () {
    cy.get('button').contains('New Question').click();

    cy.get('[data-cy="createOrEditQuestionDialog"]')
      .parent()
      .should('be.visible');

    cy.get('span.headline').should('contain', 'New Question');

    cy.get(
      '[data-cy="questionTitleTextArea"]'
    ).type('Cypress Question Example - 01', { force: true });
    cy.get(
      '[data-cy="questionQuestionTextArea"]'
    ).type('Cypress Question Example - Content - 01', { force: true });

    cy.get('[data-cy="questionOptionsInput"')
      .should('have.length', 4)
      .each(($el, index, $list) => {
        cy.get($el).within(($ls) => {
          cy.get(`[data-cy="Option${index + 1}"]`).type('Option ' + index);
        });
      });
    cy.get(`[data-cy="Checkbox3`).check({ force: true });
      
    cy.route('POST', '/courses/*/questions/').as('postQuestion');

    cy.get('button').contains('Save').click();

    cy.wait('@postQuestion').its('status').should('eq', 200);

    cy.get('[data-cy="questionTitleGrid"]')
      .first()
      .should('contain', 'Cypress Question Example - 01');

    validateQuestionFull(
      'Cypress Question Example - 01',
      'Cypress Question Example - Content - 01'
    );
  });

  it('Can view question (with button)', function () {
    cy.get('tbody tr')
      .first()
      .within(($list) => {
        cy.get('button').contains('visibility').click();
      });

    validateQuestion(
      'Cypress Question Example - 01',
      'Cypress Question Example - Content - 01'
    );

    cy.get('button').contains('close').click();
  });

  it('Can view question (with click)', function () {
    cy.get('[data-cy="questionTitleGrid"]').first().click();

    validateQuestion(
      'Cypress Question Example - 01',
      'Cypress Question Example - Content - 01'
    );

    cy.get('button').contains('close').click();
  });

  it('Can update title (with right-click)', function () {
    cy.route('PUT', '/questions/*').as('updateQuestion');

    cy.get('[data-cy="questionTitleGrid"]').first().rightclick();

    cy.get('[data-cy="createOrEditQuestionDialog"]')
      .parent()
      .should('be.visible')
      .within(($list) => {
        cy.get('span.headline').should('contain', 'Edit Question');

        cy.get('[data-cy="questionTitleTextArea"]')
          .clear({ force: true })
          .type('Cypress Question Example - 01 - Edited', { force: true });

        cy.get('button').contains('Save').click();
      });

    cy.wait('@updateQuestion').its('status').should('eq', 200);

    cy.get('[data-cy="questionTitleGrid"]')
      .first()
      .should('contain', 'Cypress Question Example - 01 - Edited');

    validateQuestionFull(
      (title = 'Cypress Question Example - 01 - Edited'),
      (content = 'Cypress Question Example - Content - 01')
    );
  });

  it('Can update content (with button)', function () {
    cy.route('PUT', '/questions/*').as('updateQuestion');

    cy.get('tbody tr')
      .first()
      .within(($list) => {
        cy.get('button').contains('edit').click();
      });

    cy.get('[data-cy="createOrEditQuestionDialog"]')
      .parent()
      .should('be.visible')
      .within(($list) => {
        cy.get('span.headline').should('contain', 'Edit Question');

        cy.get('[data-cy="questionQuestionTextArea"]')
          .clear({ force: true })
          .type('Cypress New Content For Question!', { force: true });

        cy.get('button').contains('Save').click();
      });

    cy.wait('@updateQuestion').its('status').should('eq', 200);

    validateQuestionFull(
      (title = 'Cypress Question Example - 01 - Edited'),
      (content = 'Cypress New Content For Question!')
    );
  });

  // missing update all with questions as well and change data. Should also be tested for errors :D

  it('Can duplicate question', function () {
    cy.get('tbody tr')
      .first()
      .within(($list) => {
        cy.get('button').contains('cached').click();
      });

    cy.get('[data-cy="createOrEditQuestionDialog"]')
      .parent()
      .should('be.visible');

    cy.get('span.headline').should('contain', 'New Question');

    cy.get('[data-cy="questionTitleTextArea"]')
      .should('have.value', 'Cypress Question Example - 01 - Edited')
      .type('{end} - DUP', { force: true });
    cy.get('[data-cy="questionQuestionTextArea"]').should(
      'have.value',
      'Cypress New Content For Question!'
    );

    cy.get('[data-cy="questionOptionsInput"')
      .should('have.length', 4)
      .each(($el, index, $list) => {
        cy.get($el).within(($ls) => {
          cy.get('textarea').should('have.value', 'Option ' + index);
        });
      });

    cy.route('POST', '/courses/*/questions/').as('postQuestion');

    cy.get('button').contains('Save').click();

    cy.wait('@postQuestion').its('status').should('eq', 200);

    cy.get('[data-cy="questionTitleGrid"]')
      .first()
      .should('contain', 'Cypress Question Example - 01 - Edited - DUP');

    validateQuestionFull(
      'Cypress Question Example - 01 - Edited - DUP',
      'Cypress New Content For Question!'
    );
  });

  it('Can update question to ordered', function () {
    cy.route('PUT', '/questions/*').as('updateQuestion');

    cy.get('tbody tr')
      .first()
      .within(($list) => {
        cy.get('button').contains('edit').click();
      });

    cy.get('[data-cy="createOrEditQuestionDialog"]')
      .parent()
      .should('be.visible')
      .within(($list) => {
        cy.get('span.headline').should('contain', 'Edit Question');

        cy.get(`[data-cy="Ordered"]`).check({ force: true })
        cy.get(`[data-cy="Checkbox2"]`).check({ force: true });
        cy.get('button').contains('Save').click();
      });

    cy.wait('@updateQuestion').its('status').should('eq', 200);
  });

  it('Can delete created question', function () {
    cy.route('DELETE', '/questions/*').as('deleteQuestion');
    cy.get('tbody tr')
      .first()
      .within(($list) => {
        cy.get('button').contains('delete').click();
      });

    cy.wait('@deleteQuestion').its('status').should('eq', 200);
  });

  it('Creates a new multiple choice question with only 2 options', function () {
    cy.get('button').contains('New Question').click();

    cy.get('[data-cy="createOrEditQuestionDialog"]')
      .parent()
      .should('be.visible');

    cy.get('span.headline').should('contain', 'New Question');

    cy.get(
      '[data-cy="questionTitleTextArea"]'
    ).type('Cypress Question Example - 01 (2 Options)', { force: true });
    cy.get('[data-cy="questionQuestionTextArea"]').type(
      'Cypress Question Example - Content - 01 (2 Options)',
      {
        force: true,
      }
    );

    cy.get('[data-cy="questionOptionsInput"').should('have.length', 4);

    cy.get(`[data-cy="Option1"]`).type('Option2 0');
    cy.get(`[data-cy="Checkbox1"]`).check({ force: true });
    cy.get(`[data-cy="Option2"]`).type('Option2 1');

    cy.get(`[data-cy="Delete4"]`).click({ force: true });
    cy.get(`[data-cy="Delete3"]`).click({ force: true });

    cy.route('POST', '/courses/*/questions/').as('postQuestion');

    cy.get('button').contains('Save').click();

    cy.wait('@postQuestion').its('status').should('eq', 200);

    cy.get('[data-cy="questionTitleGrid"]')
      .first()
      .should('contain', 'Cypress Question Example - 01');

    validateQuestionFull(
      'Cypress Question Example - 01 (2 Options)',
      'Cypress Question Example - Content - 01 (2 Options)',
      'Option2 ',
      0
    );
  });

  it('Creates a new multiple choice question with 10 options', function () {
    cy.get('button').contains('New Question').click();

    cy.get('[data-cy="createOrEditQuestionDialog"]')
      .parent()
      .should('be.visible');

    cy.get('span.headline').should('contain', 'New Question');

    cy.get(
      '[data-cy="questionTitleTextArea"]'
    ).type('Cypress Question Example - 01 (10 Options)', { force: true });
    cy.get('[data-cy="questionQuestionTextArea"]').type(
      'Cypress Question Example - Content - 01 (10 Options)',
      {
        force: true,
      }
    );

    cy.get('[data-cy="addOptionMultipleChoice"]').click({ force: true }); // 5
    cy.get('[data-cy="addOptionMultipleChoice"]').click({ force: true }); // 6
    cy.get('[data-cy="addOptionMultipleChoice"]').click({ force: true }); // 7
    cy.get('[data-cy="addOptionMultipleChoice"]').click({ force: true }); // 8
    cy.get('[data-cy="addOptionMultipleChoice"]').click({ force: true }); // 9
    cy.get('[data-cy="addOptionMultipleChoice"]').click({ force: true }); // 10

    cy.get('[data-cy="questionOptionsInput"')
      .should('have.length', 10)
      .each(($el, index, $list) => {
        cy.get($el).within(($ls) => {
          if (index === 6) {
            cy.get(`[data-cy="Checkbox${index + 1}"]`).check({ force: true });
          }
          cy.get(`[data-cy="Option${index + 1}"]`).type('Option10 ' + index);
        });
      });

    cy.route('POST', '/courses/*/questions/').as('postQuestion');

    cy.get('button').contains('Save').click();

    cy.wait('@postQuestion').its('status').should('eq', 200);

    cy.get('[data-cy="questionTitleGrid"]')
      .first()
      .should('contain', 'Cypress Question Example - 01');

    validateQuestionFull(
      'Cypress Question Example - 01 (10 Options)',
      'Cypress Question Example - Content - 01 (10 Options)',
      'Option10 ',
      6
    );
  });


  it('Creates a new multiple choice question with order', function () {
    cy.get('button').contains('New Question').click();

    cy.get('[data-cy="createOrEditQuestionDialog"]')
      .parent()
      .should('be.visible');

    cy.get('span.headline').should('contain', 'New Question');

    cy.get(
      '[data-cy="questionTitleTextArea"]'
    ).type('Cypress Question Example - Ordered', { force: true });
    cy.get(
      '[data-cy="questionQuestionTextArea"]'
    ).type('Cypress Question Example - Content - Ordered', { force: true });

    cy.get(`[data-cy="Ordered"]`).check({ force: true })

    cy.get('[data-cy="questionOptionsInput"')
      .should('have.length', 4)

    cy.get(`[data-cy="Option1"]`).type('Option Wrong #1');
    cy.get(`[data-cy="Option2"]`).type('Option Wrong #2');
    cy.get(`[data-cy="Option3"]`).type('Option Correct #1');
    cy.get(`[data-cy="Option4"]`).type('Option Correct #2');
    cy.get(`[data-cy="Checkbox3`).check({ force: true });
    cy.get(`[data-cy="Checkbox4`).check({ force: true });
      
    cy.route('POST', '/courses/*/questions/').as('postQuestion');

    cy.get('button').contains('Save').click();

    cy.wait('@postQuestion').its('status').should('eq', 200);

    cy.get('[data-cy="questionTitleGrid"]')
      .first()
      .should('contain', 'Cypress Question Example - Ordered');

      cy.get('[data-cy="questionTitleGrid"]').first().click();

      cy.get('[data-cy="showQuestionDialog"]')
        .should('be.visible')
        .within(($ls) => {
          cy.get('.headline').should('contain', 'Cypress Question Example - Ordered');
          cy.get('span > p').should('contain', 'Cypress Question Example - Content - Ordered');
          cy.get('li').eq(0).should('contain','[★1]')
          cy.get('li').eq(1).should('contain','[★2]')
          cy.get('li').eq(2).should('not.contain','★')
          cy.get('li').eq(3).should('not.contain','★')
        });

      cy.get('button').contains('close').click();
  });

  it('Can\'t create question without correct option', function () {
    cy.get('button').contains('New Question').click();

      cy.get('[data-cy="questionTitleTextArea"]')
        .type('Cypress Question Example - Error', { force: true });
      cy.get(
        '[data-cy="questionQuestionTextArea"]'
      ).type('Cypress Question Example - Content - Error', { force: true });

      cy.get(`[data-cy="Option1"]`).type('Option 1');
      cy.get(`[data-cy="Option2"]`).type('Option 2');
      cy.get(`[data-cy="Option3"]`).type('Option 3');
      cy.get(`[data-cy="Option4"]`).type('Option 4');
        
      cy.route('POST', '/courses/*/questions/').as('postQuestion');

      cy.get('button').contains('Save').click();

      cy.wait('@postQuestion').its('status').should('eq', 400);
      cy.contains('Error: Questions need to have at least 1 correct option');
  });

  it('Can\'t create ordered question with only 1 correct option', function () {
    cy.get('button').contains('New Question').click();

      cy.get('[data-cy="questionTitleTextArea"]')
        .type('Cypress Question Example - Error', { force: true });
      cy.get(
        '[data-cy="questionQuestionTextArea"]'
      ).type('Cypress Question Example - Content - Error', { force: true });

      cy.get(`[data-cy="Ordered"]`).check({ force: true })

      cy.get(`[data-cy="Option1"]`).type('Option 1');
      cy.get(`[data-cy="Option2"]`).type('Option 2');
      cy.get(`[data-cy="Option3"]`).type('Option 3');
      cy.get(`[data-cy="Option4"]`).type('Option 4');
      cy.get(`[data-cy="Checkbox1`).check({ force: true });
        
      cy.route('POST', '/courses/*/questions/').as('postQuestion');

      cy.get('button').contains('Save').click();

      cy.wait('@postQuestion').its('status').should('eq', 400);
      cy.contains('Error: Order questions need to have at least 2 ordered correct options.');
  });
});