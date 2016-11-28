(function() {
    'use strict';

    angular
        .module('holmesaggregatorApp')
        .controller('OutboundMessagesDeleteController',OutboundMessagesDeleteController);

    OutboundMessagesDeleteController.$inject = ['$uibModalInstance', 'entity', 'OutboundMessages'];

    function OutboundMessagesDeleteController($uibModalInstance, entity, OutboundMessages) {
        var vm = this;

        vm.outboundMessages = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OutboundMessages.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
