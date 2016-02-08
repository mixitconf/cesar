(function () {

    'use strict';
    /*global componentHandler */

    angular.module('cesar-cfp').controller('CfpTalkCtrl', function ($http, $state, $stateParams, $timeout, account) {
        'ngInject';

        var ctrl = this;



        if ($stateParams.id) {
            $http
                .get('app/cfp/proposal/' + $stateParams.id)
                .then(function (response) {
                    ctrl.proposal = response.data;
                    ctrl.check();
                });
        }

        if (!ctrl.proposal) {
            ctrl.proposal = {
                interests: []
            };
        }
        ctrl.account = account;

        var i8nKeys = {
            categories: 'view.cfp.category.',
            status: 'view.cfp.status.',
            formats: 'view.cfp.format.',
            types: 'view.cfp.typeSession.',
            levels: 'view.cfp.level.',
            maxAttendees: 'view.cfp.nbattendees.'
        };

        $http.get('app/cfp/param').then(function (response) {
            response.data.forEach(function (elt) {
                ctrl[elt.key] = [];
                elt.value.forEach(function (param) {
                    ctrl[elt.key][param] = i8nKeys[elt.key] + param;
                });
            });
        });

        ctrl.goback = function () {
            $state.go('cfp');
        };

        ctrl.addInterest = function (value) {
            if (value) {
                var canPush;
                if (!ctrl.proposal.interests) {
                    canPush = true;
                    ctrl.proposal.interests = [];
                }
                else {
                    canPush = ctrl.proposal.interests.filter(function (elt) {
                            return elt.name === value;
                        }).length === 0;
                }
                if (canPush) {
                    ctrl.proposal.interests.push({name: value});
                }
                ctrl.save();
            }
        };

        ctrl.addSpeaker = function (value) {
            if (value) {
                var canPush;
                if (!ctrl.proposal.speakers) {
                    canPush = true;
                    ctrl.proposal.speakers = [];
                }
                else {
                    canPush = ctrl.proposal.speakers.filter(function (elt) {
                            return elt.id === value.value;
                        }).length === 0;
                }
                if (canPush) {
                    ctrl.proposal.speakers.push({id: value.value, name: value.key});
                }
                ctrl.save();
            }
        };

        ctrl.removeSpeaker = function (value) {
            ctrl.proposal.speakers.splice(ctrl.proposal.speakers.indexOf(value), 1);
        };

        ctrl.removeInterest = function (value) {
            ctrl.proposal.interests.splice(ctrl.proposal.interests.indexOf(value), 1);
        };

        function _save(){
            delete ctrl.errorMessage;
            delete ctrl.warningMessage;
            delete ctrl.confirm;
            $http
              .post('app/cfp/proposal', angular.copy(ctrl.proposal), {ignoreErrorRedirection: 'ignoreErrorRedirection'})
              .then(function (response) {
                  ctrl.proposal = response.data;
                  return $http.post('app/cfp/proposal/check', angular.copy(ctrl.proposal), {ignoreErrorRedirection: 'ignoreErrorRedirection'});
              })
              .then(function (response) {
                  ctrl.warningMessage = response.data;
                  refresh();
              })
              .catch(function () {
                  ctrl.errorMessage = 'UNDEFINED';
              });

        }

        ctrl.save = function (spinner) {
            console.log(spinner)
            if(spinner==='off'){
                _save();
            }
        };

        ctrl.check = function (spinner) {
            if(ctrl.proposal.id && spinner==='off'){
                delete ctrl.warningMessage;
                delete ctrl.confirm;

                $http
                  .post('app/cfp/proposal/check', angular.copy(ctrl.proposal), {ignoreErrorRedirection: 'ignoreErrorRedirection'})
                  .then(function (response) {
                      ctrl.warningMessage = response.data;
                      refresh();
                  })
                  .catch(function () {
                      ctrl.errorMessage = 'UNDEFINED';
                  });
            }
        };

        ctrl.delete = function () {
            delete ctrl.errorMessage;
            if (ctrl.confirm && ctrl.confirm.display && ctrl.confirm.message) {

                if (ctrl.confirm.message.toLowerCase() === account.firstname.toLowerCase()) {
                    $http
                        .delete('app/cfp/proposal/' + ctrl.proposal.id, {ignoreErrorRedirection: 'ignoreErrorRedirection'})
                        .then(function () {
                            $state.go('cfp');
                        })
                        .catch(function () {
                            ctrl.errorMessage = 'UNDEFINED';
                        });
                }
                else {
                    ctrl.errorMessage = 'CONFIRMDELETE';
                }
            }
            else {
                ctrl.confirm = {
                    display: true
                };
            }
        };
        function refresh() {
            $timeout(function () {
                componentHandler.upgradeAllRegistered();
            }, 1000);
        }

    });
})();
