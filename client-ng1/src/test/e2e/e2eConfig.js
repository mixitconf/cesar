//  Add e2e module to main pb module
angular.module('cesar').requires.push('cesar.e2e');

angular.module('cesar.e2e', ['ngMockE2E']).run(function ($httpBackend) {

    // I18n
    $httpBackend.whenGET(/i18n\/.*-fr-FR.json/).respond({'fr': {'New page': 'Nouvelle page'}});

    //Example
    $httpBackend.whenGET('app/test').respond(200);


  });
