(function() {
    'use strict';

    angular
        .module('holmesaggregatorApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('outbound-messages', {
            parent: 'entity',
            url: '/outbound-messages',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'OutboundMessages'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/outbound-messages/outbound-messages.html',
                    controller: 'OutboundMessagesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('outbound-messages-detail', {
            parent: 'entity',
            url: '/outbound-messages/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'OutboundMessages'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/outbound-messages/outbound-messages-detail.html',
                    controller: 'OutboundMessagesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'OutboundMessages', function($stateParams, OutboundMessages) {
                    return OutboundMessages.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'outbound-messages',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('outbound-messages-detail.edit', {
            parent: 'outbound-messages-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/outbound-messages/outbound-messages-dialog.html',
                    controller: 'OutboundMessagesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OutboundMessages', function(OutboundMessages) {
                            return OutboundMessages.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('outbound-messages.new', {
            parent: 'outbound-messages',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/outbound-messages/outbound-messages-dialog.html',
                    controller: 'OutboundMessagesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                messageId: null,
                                groupId: null,
                                createdAt: null,
                                payload: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('outbound-messages', null, { reload: 'outbound-messages' });
                }, function() {
                    $state.go('outbound-messages');
                });
            }]
        })
        .state('outbound-messages.edit', {
            parent: 'outbound-messages',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/outbound-messages/outbound-messages-dialog.html',
                    controller: 'OutboundMessagesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OutboundMessages', function(OutboundMessages) {
                            return OutboundMessages.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('outbound-messages', null, { reload: 'outbound-messages' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('outbound-messages.delete', {
            parent: 'outbound-messages',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/outbound-messages/outbound-messages-delete-dialog.html',
                    controller: 'OutboundMessagesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OutboundMessages', function(OutboundMessages) {
                            return OutboundMessages.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('outbound-messages', null, { reload: 'outbound-messages' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
