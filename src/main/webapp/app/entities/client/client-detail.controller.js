(function() {
    'use strict';

    angular
        .module('holmesaggregatorApp')
        .controller('ClientDetailController', ClientDetailController);

    ClientDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Client'];

    function ClientDetailController($scope, $rootScope, $stateParams, previousState, entity, Client) {
        var vm = this;

        vm.client = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('holmesaggregatorApp:clientUpdate', function(event, result) {
            vm.client = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
