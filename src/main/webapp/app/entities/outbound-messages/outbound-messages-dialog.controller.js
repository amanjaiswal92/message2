(function() {
    'use strict';

    angular
        .module('holmesaggregatorApp')
        .controller('OutboundMessagesDialogController', OutboundMessagesDialogController);

    OutboundMessagesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'OutboundMessages'];

    function OutboundMessagesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, OutboundMessages) {
        var vm = this;

        vm.outboundMessages = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.outboundMessages.id !== null) {
                OutboundMessages.update(vm.outboundMessages, onSaveSuccess, onSaveError);
            } else {
                OutboundMessages.save(vm.outboundMessages, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('holmesaggregatorApp:outboundMessagesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdAt = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
