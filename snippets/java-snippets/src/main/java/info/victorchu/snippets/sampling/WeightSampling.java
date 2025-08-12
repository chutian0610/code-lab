package info.victorchu.snippets.sampling;

import org.apache.commons.rng.sampling.CollectionSampler;
import org.apache.commons.rng.simple.RandomSource;
import org.apache.commons.rng.sampling.DiscreteProbabilityCollectionSampler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeightSampling {
    public static void main(String[] args) {
        // 加权随机选择
        Map<String,Double> weightMap = new HashMap<>(4);
        weightMap.put("A",0.2);
        weightMap.put("B",0.3);
        weightMap.put("C",0.4);
        weightMap.put("D",0.0);

        DiscreteProbabilityCollectionSampler<String> sampler = new DiscreteProbabilityCollectionSampler<>(RandomSource.JDK.create(), weightMap);
        System.out.println(sampler.sample());

        // 随机选择
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        CollectionSampler<String> collectionSampler = new CollectionSampler<>(RandomSource.JDK.create(), list);
        System.out.println(collectionSampler.sample());
    }
}
