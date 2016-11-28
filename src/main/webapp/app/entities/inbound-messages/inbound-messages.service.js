(function() {
    'use strict';
    angular
        .module('holmesaggregatorApp')
        .factory('InboundMessages', InboundMessages);

    InboundMessages.$inject = ['$resource', 'DateUtils'];

    function InboundMessages ($resource, DateUtils) {
        var resourceUrl =  'api/inbound-messages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.created_at = DateUtils.convertLocalDateFromServer(data.created_at);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.created_at = DateUtils.convertLocalDateToServer(data.created_at);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.created_at = DateUtils.convertLocalDateToServer(data.created_at);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
