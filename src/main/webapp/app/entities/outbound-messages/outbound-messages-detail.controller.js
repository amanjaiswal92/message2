(function() {
    'use strict';

    angular
        .module('holmesaggregatorApp')
        .controller('OutboundMessagesDetailController', OutboundMessagesDetailController);

    OutboundMessagesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'OutboundMessages'];

    function OutboundMessagesDetailController($scope, $rootScope, $stateParams, previousState, entity, OutboundMessages) {
        var vm = this;

        vm.outboundMessages = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('holmesaggregatorApp:outboundMessagesUpdate', function(event, result) {
            vm.outboundMessages = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
