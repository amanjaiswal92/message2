(function() {
    'use strict';

    angular
        .module('holmesaggregatorApp')
        .controller('InboundMessagesController', InboundMessagesController);

    InboundMessagesController.$inject = ['$scope', '$state', 'InboundMessages'];

    function InboundMessagesController ($scope, $state, InboundMessages) {
        var vm = this;
        
        vm.inboundMessages = [];

        loadAll();

        function loadAll() {
            InboundMessages.query(function(result) {
                vm.inboundMessages = result;
            });
        }
    }
})();
