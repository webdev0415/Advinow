package com.advinow.mica.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.resilience4j.retry.annotation.Retry;

import com.advinow.mica.domain.queryresult.DataframeQueryResultEntity;
import com.advinow.mica.model.IllnessStatusModel;
import com.advinow.mica.model.dataframe.DataFrameModel;
import com.advinow.mica.repositories.DataFrameRepository;
import com.advinow.mica.services.DataFrameIllnessService;

/**
 * 
 * @author Govinda Reddy
 *
 */
@Service
@Retry(name = "neo4j")
public class DataFrameIllnessServiceImpl implements DataFrameIllnessService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	DataFrameRepository dataFrameRepository;

	@Override
	public IllnessStatusModel updateDataFrameInfo(DataFrameModel dataFrameModel) {
		IllnessStatusModel status = new IllnessStatusModel();
		Boolean flag = false;
		status.setStatus("Update Failed.............");

		// illnesses update to include in the data frame
		Map<String, Integer> icd10Codes = dataFrameModel.getIcd10Codes();
		Collection<Map<String, Object>> literalMap = new ArrayList<Map<String, Object>>();
		if (icd10Codes != null && !icd10Codes.isEmpty()) {
			dataFrameRepository.updateDFIllnessesToFalse();
			flag = true;
			icd10Codes.forEach((k, v) -> {
				Map<String, Object> illnesses = new Hashtable<String, Object>();
				illnesses.put("icd10Code", k);
				illnesses.put("version", v);
				literalMap.add(illnesses);
			});
			dataFrameRepository.updateDataFrameIllnesses(literalMap);
		}

		// To update all the clusters
		Map<String, Integer> clusters = dataFrameModel.getClusters();
		Collection<Map<String, Object>> illnessClusters = new ArrayList<Map<String, Object>>();
		if (clusters != null && !clusters.isEmpty()) {
			flag = true;
			clusters.forEach((k, v) -> {
				Map<String, Object> cluster = new Hashtable<String, Object>();
				cluster.put("icd10Code", k);
				cluster.put("cluster", v);
				illnessClusters.add(cluster);
			});
			dataFrameRepository.updateDFIllnessClusters(illnessClusters);
		}

		// To update all the priors
		Map<String, Float> priors = dataFrameModel.getPriors();
		Collection<Map<String, Object>> illnessPriors = new ArrayList<Map<String, Object>>();
		if (priors != null && !priors.isEmpty()) {
			flag = true;
			priors.forEach((k, v) -> {
				Map<String, Object> prior = new Hashtable<String, Object>();
				prior.put("icd10Code", k);
				prior.put("prior", v);
				illnessPriors.add(prior);
			});
			dataFrameRepository.updateDFIllnessPriors(illnessPriors);
		}

		// to update dcl for symptoms and descriptors

		List<DataframeQueryResultEntity> symptomModelDcls = dataFrameModel.getSymptomDcls();
		Collection<Map<String, Object>> descriptorDcls = new ArrayList<Map<String, Object>>();
		Collection<Map<String, Object>> symptomDcls = new ArrayList<Map<String, Object>>();

		for (DataframeQueryResultEntity dfSymptomModel : symptomModelDcls) {
			if (dfSymptomModel.getDescriptorID() != null) {
				Map<String, Object> descriptor = new Hashtable<String, Object>();
				descriptor.put("code", dfSymptomModel.getDescriptorID());
				descriptor.put("m_antithesis", dfSymptomModel.getDcl());
				descriptorDcls.add(descriptor);
			} else {
				Map<String, Object> symptom = new Hashtable<String, Object>();
				symptom.put("code", dfSymptomModel.getSymptomID());
				symptom.put("antithesis", dfSymptomModel.getDcl());
				symptomDcls.add(symptom);
			}
		}

		if (!descriptorDcls.isEmpty()) {
			flag = true;
			dataFrameRepository.updateDataFrameDescriptors(descriptorDcls);
		}

		if (!symptomDcls.isEmpty()) {
			flag = true;
			dataFrameRepository.updateDataFrameSymptoms(symptomDcls);
		}

		if (dataFrameModel.getSymptoms() != null && !dataFrameModel.getSymptoms().isEmpty()) {
			dataFrameRepository.updateDFSymptomsToFalse();
			dataFrameRepository.updateDataFrameSymptoms(dataFrameModel.getSymptoms());
			flag = true;
		}

		if (flag) {
			status.setStatus("Updated Successfully.............");
		}
		return status;
	}

	@Override
	public DataFrameModel getSymptomsIllnesses() {
		// TODO Auto-generated method stub
		DataFrameModel dataFrameModel = new DataFrameModel();

		List<DataframeQueryResultEntity> illnessPriorsClusters = dataFrameRepository.getDataFramePriorsANDClusters();

		if (illnessPriorsClusters != null) {
			// priors = illnessPriors.getLiteralMap().stream().collect(Collectors.toMap(s ->
			// (String) s.get("icd10Code"), s-> ((Double) s.get("prior")).floatValue()));
			// dataFrameModel.setPriors(illnessPriorsClusters.stream().collect(Collectors.toMap(GenericQueryResultEntity::getIcd10code,
			// GenericQueryResultEntity::getPrior, (oldValue, newValue) -> newValue)));
			dataFrameModel.setClusters(
					illnessPriorsClusters.stream().collect(Collectors.toMap(DataframeQueryResultEntity::getIcd10code,
							DataframeQueryResultEntity::getCluster, (oldValue, newValue) -> newValue)));
		}

		List<DataframeQueryResultEntity> dfIllnesses = dataFrameRepository.getDataFrameIllnesses();

		if (dfIllnesses != null) {
			// elemenate duplicates
			// icd10Codes =
			// illnessDf.getLiteralMap().stream().distinct().collect(Collectors.toMap(s ->
			// (String) s.get("icd10Code"), s-> (Integer) s.get("version")));

			dataFrameModel.setIcd10Codes(
					dfIllnesses.stream().collect(Collectors.toMap(DataframeQueryResultEntity::getIcd10code,
							DataframeQueryResultEntity::getVersion, (oldValue, newValue) -> newValue)));
		}

		Set<String> dfSymptoms = dataFrameRepository.getDataFrameSymptoms();
		if (dfSymptoms != null) {
			dataFrameModel.setSymptoms(dfSymptoms);
		}

		List<DataframeQueryResultEntity> dcls = dataFrameRepository.getSymptomsDescriptosDcls();
		if (dcls != null && !dcls.isEmpty()) {
			dataFrameModel.setSymptomDcls(dcls);
		}

		// config.setSymptoms();
		return dataFrameModel;
	}

	@Override
	public DataFrameModel getAllAciveIllnesses() {
		// TODO Auto-generated method stub
		DataFrameModel dataFrameModel = new DataFrameModel();
		List<DataframeQueryResultEntity> dfIllnesses = dataFrameRepository.getDataFrameIllnesses();
		if (dfIllnesses != null) {
			dataFrameModel.setIcd10Codes(
					dfIllnesses.stream().collect(Collectors.toMap(DataframeQueryResultEntity::getIcd10code,
							DataframeQueryResultEntity::getVersion, (oldValue, newValue) -> newValue)));
		}
		return dataFrameModel;
	}

	@Override
	public DataFrameModel getAllAciveSymptoms() {
		// TODO Auto-generated method stub
		DataFrameModel dataFrameModel = new DataFrameModel();
		Set<String> dfSymptoms = dataFrameRepository.getDataFrameSymptoms();
		if (dfSymptoms != null) {
			dataFrameModel.setSymptoms(dfSymptoms);
		}
		return dataFrameModel;
	}
}