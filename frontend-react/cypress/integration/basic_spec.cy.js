describe('Basic smoke', () => {
  it('loads the app', () => {
    cy.visit('/');
    cy.title();
  });
});