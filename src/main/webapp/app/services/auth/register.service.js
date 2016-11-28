(function () {
    'use strict';

    angular
        .module('holmesaggregatorApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
