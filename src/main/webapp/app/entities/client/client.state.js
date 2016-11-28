(function() {
    'use strict';

    angular
        .module('holmesaggregatorApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('client', {
            parent: 'entity',
            url: '/client',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Clients'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/client/clients.html',
                    controller: 'ClientController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('client-detail', {
            parent: 'entity',
            url: '/client/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Client'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/client/client-detail.html',
                    controller: 'ClientDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Client', function($stateParams, Client) {
                    return Client.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'client',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('client-detail.edit', {
            parent: 'client-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client/client-dialog.html',
                    controller: 'ClientDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Client', function(Client) {
                            return Client.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('client.new', {
            parent: 'client',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client/client-dialog.html',
                    controller: 'ClientDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                topic: null,
                                read_offset: null,
                                page_size: null,
                                uri: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('client', null, { reload: 'client' });
                }, function() {
                    $state.go('client');
                });
            }]
        })
        .state('client.edit', {
            parent: 'client',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client/client-dialog.html',
                    controller: 'ClientDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Client', function(Client) {
                            return Client.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('client', null, { reload: 'client' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('client.delete', {
            parent: 'client',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client/client-delete-dialog.html',
                    controller: 'ClientDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Client', function(Client) {
                            return Client.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('client', null, { reload: 'client' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
