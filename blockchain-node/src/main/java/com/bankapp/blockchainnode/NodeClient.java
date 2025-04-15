package com.bankapp.blockchainnode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Component
public class NodeClient {
    private final DiscoveryClient discoveryClient;
    private final RestTemplate restTemplate;
    @Autowired
    public NodeClient(DiscoveryClient discoveryClient, RestTemplate restTemplate) {
        this.discoveryClient = discoveryClient;
        this.restTemplate = restTemplate;
    }

    public boolean validateTransaction(String nodeId, Transaction transaction) {
        List<ServiceInstance> instances = discoveryClient.getInstances("blockchain-node");
        System.out.println("[DEBUG] Found " + instances.size() + " nodes for validation");

        int approvals = 0;
        for (ServiceInstance instance : instances) {
            System.out.println("[DEBUG] Validating with node: " + instance.getUri());
            Boolean result = restTemplate.postForObject(instance.getUri() + "/validate", transaction, Boolean.class);
            if (result != null && result) approvals++;
        }

        System.out.println("[DEBUG] Received " + approvals + "/" + instances.size() + " approvals");
        return approvals > (instances.size() * 2 / 3);
    }
}
