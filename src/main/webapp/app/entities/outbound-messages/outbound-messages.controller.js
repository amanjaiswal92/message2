(function() {
    'use strict';

    angular
        .module('holmesaggregatorApp')
        .controller('OutboundMessagesController', OutboundMessagesController);

    OutboundMessagesController.$inject = ['$scope', '$state', 'OutboundMessages'];

    function OutboundMessagesController ($scope, $state, OutboundMessages) {
        var vm = this;
        
        vm.outboundMessages = [];

        loadAll();

        function loadAll() {
            OutboundMessages.query(function(result) {
                vm.outboundMessages = result;
            });
        }
    }
})();
