(function() {
    'use strict';

    angular
        .module('holmesaggregatorApp')
        .controller('InboundMessagesDialogController', InboundMessagesDialogController);

    InboundMessagesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'InboundMessages'];

    function InboundMessagesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, InboundMessages) {
        var vm = this;

        vm.inboundMessages = entity;
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
            if (vm.inboundMessages.id !== null) {
                InboundMessages.update(vm.inboundMessages, onSaveSuccess, onSaveError);
            } else {
                InboundMessages.save(vm.inboundMessages, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('holmesaggregatorApp:inboundMessagesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.created_at = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
