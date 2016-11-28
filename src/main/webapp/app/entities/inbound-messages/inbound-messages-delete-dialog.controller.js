(function() {
    'use strict';

    angular
        .module('holmesaggregatorApp')
        .controller('InboundMessagesDeleteController',InboundMessagesDeleteController);

    InboundMessagesDeleteController.$inject = ['$uibModalInstance', 'entity', 'InboundMessages'];

    function InboundMessagesDeleteController($uibModalInstance, entity, InboundMessages) {
        var vm = this;

        vm.inboundMessages = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InboundMessages.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
