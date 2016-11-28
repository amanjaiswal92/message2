(function() {
    'use strict';

    angular
        .module('holmesaggregatorApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('inbound-messages', {
            parent: 'entity',
            url: '/inbound-messages',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'InboundMessages'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/inbound-messages/inbound-messages.html',
                    controller: 'InboundMessagesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('inbound-messages-detail', {
            parent: 'entity',
            url: '/inbound-messages/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'InboundMessages'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/inbound-messages/inbound-messages-detail.html',
                    controller: 'InboundMessagesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'InboundMessages', function($stateParams, InboundMessages) {
                    return InboundMessages.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'inbound-messages',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('inbound-messages-detail.edit', {
            parent: 'inbound-messages-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inbound-messages/inbound-messages-dialog.html',
                    controller: 'InboundMessagesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InboundMessages', function(InboundMessages) {
                            return InboundMessages.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('inbound-messages.new', {
            parent: 'inbound-messages',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inbound-messages/inbound-messages-dialog.html',
                    controller: 'InboundMessagesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                messageId: null,
                                groupId: null,
                                created_at: null,
                                payload: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('inbound-messages', null, { reload: 'inbound-messages' });
                }, function() {
                    $state.go('inbound-messages');
                });
            }]
        })
        .state('inbound-messages.edit', {
            parent: 'inbound-messages',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inbound-messages/inbound-messages-dialog.html',
                    controller: 'InboundMessagesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InboundMessages', function(InboundMessages) {
                            return InboundMessages.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('inbound-messages', null, { reload: 'inbound-messages' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('inbound-messages.delete', {
            parent: 'inbound-messages',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inbound-messages/inbound-messages-delete-dialog.html',
                    controller: 'InboundMessagesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InboundMessages', function(InboundMessages) {
                            return InboundMessages.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('inbound-messages', null, { reload: 'inbound-messages' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
