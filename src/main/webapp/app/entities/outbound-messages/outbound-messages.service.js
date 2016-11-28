(function() {
    'use strict';
    angular
        .module('holmesaggregatorApp')
        .factory('OutboundMessages', OutboundMessages);

    OutboundMessages.$inject = ['$resource', 'DateUtils'];

    function OutboundMessages ($resource, DateUtils) {
        var resourceUrl =  'api/outbound-messages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdAt = DateUtils.convertDateTimeFromServer(data.createdAt);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
