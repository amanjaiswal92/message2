(function() {
    'use strict';

    angular
        .module('holmesaggregatorApp')
        .controller('InboundMessagesDetailController', InboundMessagesDetailController);

    InboundMessagesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'InboundMessages'];

    function InboundMessagesDetailController($scope, $rootScope, $stateParams, previousState, entity, InboundMessages) {
        var vm = this;

        vm.inboundMessages = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('holmesaggregatorApp:inboundMessagesUpdate', function(event, result) {
            vm.inboundMessages = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
