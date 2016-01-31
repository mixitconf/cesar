describe('Service PlanningService', function () {
  var service, $httpBackend, slots;

  beforeEach(module('cesar-planning'));


  beforeEach(inject(function ($injector) {
    $httpBackend = $injector.get('$httpBackend');
    service = $injector.get('PlanningService');

    slots = { 'Amphi1' : [
      builders
        .createSlot(1)
        .range('2016-04-21T08:00', '2016-04-21T09:10')
        .label('planning.accueil')
        .build(),
      builders
        .createSlot(2)
        .range('2016-04-21T08:00', '2016-04-21T10:30')
        .session(builders.createSession(631).title('Microplugins avec Docker').lang('fr').build())
        .build(),
      builders
        .createSlot(3)
        .range('2016-04-21T10:30', '2016-04-21T11:40')
        .session(builders.createSession(711).title('24 Minutes chrono pour bâtir une appli mobile').lang('fr').build())
        .build(),
      builders
        .createSlot(4)
        .range('2016-04-21T13:30', '2016-04-21T14:20')
        .session(builders.createSession(771).title('Sirius : un schéma vaut mieux qu\'un long discours').lang('fr').build())
        .build(),
    ]};

  }));


  it('should compute planning', function () {
    $httpBackend.expectGET('/api/planning').respond(slots);

    service.computeSlots().then(function(response){
      expect(response).toEqual(slots);
    });
    $httpBackend.flush();

  });

});