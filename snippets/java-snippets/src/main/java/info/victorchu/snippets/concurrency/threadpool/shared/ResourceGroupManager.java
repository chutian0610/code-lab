package info.victorchu.snippets.concurrency.threadpool.shared;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ResourceGroupManager{

    @Getter
    private final List<ResourceGroup> resourceGroups;
    private final Map<String,ResourceGroup> resourceGroupMap;

    public ResourceGroupManager(List<ResourceGroup> resourceGroupList) {
        List<ResourceGroup> resourceGroups = new ArrayList<>(resourceGroupList);
        this.resourceGroups = resourceGroups;
        // ResourceGroupId must be unique
        this.resourceGroupMap = resourceGroups.stream().collect(Collectors.toMap(ResourceGroup::getName, Function.identity()));
    }

    public ResourceGroup getResourceGroup(ResourceGroupManaged resourceGroupManaged)
    {
        return resourceGroupMap.get(resourceGroupManaged.getResourceGroupId());
    }
}
