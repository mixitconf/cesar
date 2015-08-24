describe('MemberCtrl', function () {
  var ctrl, $httpBackend;

  beforeEach(module('cesar-members'));

  beforeEach(inject(function ($controller, $injector) {

    $httpBackend = $injector.get('$httpBackend');

    ctrl = $controller('MemberCtrl', {
      $http : $injector.get('$http'),
      $state : {
        current : {
          data : {
            member : 'speaker'
          }
        }
      }
    });
  }));


  it('should read all the users', function () {
    $httpBackend.expectGET('/api/member/speaker').respond([
      { firstname : 'James', lastname : 'Gosling'}
    ]);
    $httpBackend.flush();
    expect(ctrl.members.length).toBe(1);
  });
});