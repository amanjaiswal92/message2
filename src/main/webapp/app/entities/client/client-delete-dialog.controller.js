(function() {
    'use strict';

    angular
        .module('holmesaggregatorApp')
        .controller('ClientDeleteController',ClientDeleteController);

    ClientDeleteController.$inject = ['$uibModalInstance', 'entity', 'Client'];

    function ClientDeleteController($uibModalInstance, entity, Client) {
        var vm = this;

        vm.client = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Client.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
