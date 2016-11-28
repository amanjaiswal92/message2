'use strict';

describe('Controller Tests', function() {

    describe('OutboundMessages Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockOutboundMessages;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockOutboundMessages = jasmine.createSpy('MockOutboundMessages');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'OutboundMessages': MockOutboundMessages
            };
            createController = function() {
                $injector.get('$controller')("OutboundMessagesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'holmesaggregatorApp:outboundMessagesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
