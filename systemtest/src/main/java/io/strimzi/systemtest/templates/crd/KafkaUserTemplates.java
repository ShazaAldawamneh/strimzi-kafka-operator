/*
 * Copyright Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.systemtest.templates.crd;

import io.strimzi.api.kafka.model.KafkaUser;
import io.strimzi.api.kafka.model.KafkaUserBuilder;
import io.strimzi.operator.common.model.Labels;
import io.strimzi.systemtest.resources.ResourceManager;

public class KafkaUserTemplates {

    private KafkaUserTemplates() {}

    public static KafkaUserBuilder tlsUser(String namespaceName, String clusterName, String name) {
        return defaultUser(namespaceName, clusterName, name)
            .withNewSpec()
                .withNewKafkaUserTlsClientAuthentication()
                .endKafkaUserTlsClientAuthentication()
            .endSpec();
    }

    public static KafkaUserBuilder tlsUser(String clusterName, String name) {
        return tlsUser(ResourceManager.kubeClient().getNamespace(), clusterName, name);
    }

    public static KafkaUserBuilder scramShaUser(String namespaceName, String clusterName, String name) {
        return defaultUser(namespaceName, clusterName, name)
            .withNewSpec()
                .withNewKafkaUserScramSha512ClientAuthentication()
                .endKafkaUserScramSha512ClientAuthentication()
            .endSpec();
    }

    public static KafkaUserBuilder scramShaUser(String clusterName, String name) {
        return scramShaUser(ResourceManager.kubeClient().getNamespace(), clusterName, name);
    }

    public static KafkaUserBuilder tlsExternalUser(final String namespaceName, final String clusterName, final String name) {
        return defaultUser(namespaceName, clusterName, name)
            .withNewSpec()
                .withNewKafkaUserTlsExternalClientAuthentication()
                .endKafkaUserTlsExternalClientAuthentication()
            .endSpec();
    }

    public static KafkaUserBuilder defaultUser(String namespaceName, String clusterName, String name) {
        return new KafkaUserBuilder()
            .withNewMetadata()
                .withClusterName(clusterName)
                .withName(name)
                .withNamespace(namespaceName)
                .addToLabels(Labels.STRIMZI_CLUSTER_LABEL, clusterName)
            .endMetadata();
    }

    public static KafkaUserBuilder defaultUser(String clusterName, String name) {
        return defaultUser(ResourceManager.kubeClient().getNamespace(), clusterName, name);
    }

    public static KafkaUserBuilder userWithQuotas(KafkaUser user, Integer prodRate, Integer consRate, Integer requestPerc, Double mutRate) {
        return new KafkaUserBuilder(user)
                .editSpec()
                    .withNewQuotas()
                        .withConsumerByteRate(consRate)
                        .withProducerByteRate(prodRate)
                        .withRequestPercentage(requestPerc)
                        .withControllerMutationRate(mutRate)
                    .endQuotas()
                .endSpec();
    }
}
