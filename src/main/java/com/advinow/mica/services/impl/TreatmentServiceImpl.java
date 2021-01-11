/**
 *
 */
package com.advinow.mica.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.resilience4j.retry.annotation.Retry;

import com.advinow.mica.domain.IllnessTreatment;
import com.advinow.mica.domain.SymptomTemplate;
import com.advinow.mica.domain.SymptomTreatment;
import com.advinow.mica.domain.TreatmentSourceInfo;
import com.advinow.mica.domain.TreatmentTypeRef;
import com.advinow.mica.exception.DataInvalidException;
import com.advinow.mica.exception.DataNotFoundException;
import com.advinow.mica.mapper.TreatmentRequestMapper;
import com.advinow.mica.mapper.TreatmentResponseMapper;
import com.advinow.mica.model.AuditSourceModel;
import com.advinow.mica.model.AuditTreatmentTypeModel;
import com.advinow.mica.model.TreatmentAuditSourcesModel;
import com.advinow.mica.model.TreatmentMainDocModel;
import com.advinow.mica.model.TreatmentMainModel;
import com.advinow.mica.model.TreatmentStatusModel;
import com.advinow.mica.repositories.DrugRepository;
import com.advinow.mica.repositories.IllnessTreatmentRespository;
import com.advinow.mica.repositories.NonDrugRepository;
import com.advinow.mica.repositories.SymptomTemplateRepository;
import com.advinow.mica.repositories.SymptomTreatmentRepository;
import com.advinow.mica.repositories.TreatmentGroupRespository;
import com.advinow.mica.repositories.TreatmentRepository;
import com.advinow.mica.repositories.TreatmentSourceInfoRepository;
import com.advinow.mica.repositories.TreatmentTypeRefRepository;
import com.advinow.mica.services.TreatmentService;

/**
 * @author Govinda Reddy
 *
 */
@Service
@Retry(name = "neo4j")
public class TreatmentServiceImpl implements TreatmentService {

	@Autowired
	IllnessTreatmentRespository illnessTreatmentRespository;

	@Autowired
	SymptomTreatmentRepository  symptomTreatmentRepository;

	@Autowired
	TreatmentTypeRefRepository  treatmentTypeRepository;

	@Autowired
	TreatmentRepository treatmentRepository;
	@Autowired
	NonDrugRepository nonDrugRepository;

	@Autowired
	DrugRepository drugRepository;

	@Autowired
	TreatmentGroupRespository treatmentGroupRespository;

	@Autowired
	SymptomTemplateRepository symptomTemplateRepository;

	@Autowired
	TreatmentSourceInfoRepository  treatmentSourceInfoRepository;

	TreatmentRequestMapper  treatmentRequestMapper = new TreatmentRequestMapper();

	TreatmentResponseMapper treatmentResponseMapper = new TreatmentResponseMapper();
	@Override
	@Transactional
	public TreatmentStatusModel creatreatTrementsForIllness(TreatmentMainModel treatmentMainModel) {
		if(treatmentMainModel == null || treatmentMainModel.getIcd10Code() == null ){
			throw new DataInvalidException("ICD10Code does not exists in the request");
		}
		String icd10Code =  treatmentMainModel.getIcd10Code().toUpperCase();
		IllnessTreatment illnessTreatment =	treatmentRequestMapper.createDbIllnessTreatment(treatmentMainModel);
		IllnessTreatment dbIllnessTrtmt = illnessTreatmentRespository.findIllnessByCode(icd10Code) ;
		TreatmentStatusModel treatmentStatusModel = new TreatmentStatusModel();
		if(dbIllnessTrtmt != null  ) {
			deleteTreatmentIllnessByCode(icd10Code);
		}
		treatmentStatusModel.setStatus("Treatment created successfully");
		illnessTreatmentRespository.save(illnessTreatment);


		return treatmentStatusModel;
	}

	@Override
	@Transactional
	public TreatmentStatusModel creatreatTrementsForSymptom(
			TreatmentMainModel treatmentMainModel) {
		if(treatmentMainModel == null ||  treatmentMainModel.getSymptomID() == null ){
			throw new DataInvalidException("SymptomID does not exists in the request");
		}
		String	symptomID = treatmentMainModel.getSymptomID().toUpperCase();
		SymptomTreatment symptomTreatment =	treatmentRequestMapper.createDbSymptomsTreatment(treatmentMainModel);
		SymptomTemplate symptomTemplate = symptomTemplateRepository.findBySymptomCode(symptomID);
		if(symptomTemplate ==null ){
			throw new DataNotFoundException("No symptom data found found for  "+ symptomID);
		}
		SymptomTreatment dbSymptomTreatment = symptomTreatmentRepository.findTreatmentBySymptomID(symptomID);
		TreatmentStatusModel treatmentStatusModel = new TreatmentStatusModel();
		if(dbSymptomTreatment != null) {
			deleteSymptomBycode(symptomID);


		}
		symptomTreatmentRepository.save(symptomTreatment);
		treatmentStatusModel.setStatus("Treatment created successfully");

		return treatmentStatusModel;
	}

	@Override
	public List<TreatmentMainModel> getIllnessTreatments() {
		List<TreatmentMainModel> listTreatments = new ArrayList<TreatmentMainModel>();
		Iterable<IllnessTreatment> dbillMainTreatments = illnessTreatmentRespository.getIllnessTreatments();
		List<TreatmentTypeRef> TreatmentTypeRefList = treatmentTypeRepository.getAllTreatmentsWithDesc(); 

		if(dbillMainTreatments != null ) {
			Iterator<IllnessTreatment> itr = dbillMainTreatments.iterator();
			while(itr.hasNext()){
				IllnessTreatment dbIllnesTreatment = itr.next();
				TreatmentMainModel treatmentMainModel = treatmentResponseMapper.createIllnessTreatment(dbIllnesTreatment,TreatmentTypeRefList,true);
				listTreatments.add(treatmentMainModel);
			}
		}
		return listTreatments;
	}

	@Override
	public List<TreatmentMainModel> getSymptomsTreatments() {
		List<TreatmentMainModel> listTreatments = new ArrayList<TreatmentMainModel>();
		Iterable<SymptomTreatment> dbSymptomsMainTreatments = symptomTreatmentRepository.getSymptomTreatments();
		List<TreatmentTypeRef> TreatmentTypeRefList = treatmentTypeRepository.getAllTreatmentsWithDesc(); 

		if(dbSymptomsMainTreatments != null ) {
			Iterator<SymptomTreatment> itr = dbSymptomsMainTreatments.iterator();
			while(itr.hasNext()){
				SymptomTreatment dbSymptomTreatment = itr.next();
				TreatmentMainModel treatmentMainModel = treatmentResponseMapper.createSymptomTreatment(dbSymptomTreatment,TreatmentTypeRefList,true);
				listTreatments.add(treatmentMainModel);
			}

		}
		return listTreatments;
	}



	@Override
	public TreatmentMainModel getTreatmentBySymptomID(String symptomID) {
		SymptomTreatment dbSymptomTreatment = symptomTreatmentRepository.findTreatmentsBySymptomID(symptomID.toUpperCase());
		List<TreatmentTypeRef> TreatmentTypeRefList = treatmentTypeRepository.getAllTreatmentsWithDesc(); 
		//return	treatmentResponseMapper.createSymptomTreatment(dbSymptomTreatment,treatmentTypeRepository,true);
		return	treatmentResponseMapper.createSymptomTreatment(dbSymptomTreatment,TreatmentTypeRefList,true);
	}

	@Override
	public TreatmentMainModel getTreatmentByICD10Code(String icd10Code) {
		IllnessTreatment treatmentData = illnessTreatmentRespository.findTreatmentsByIcd10Code(icd10Code.toUpperCase());
		List<TreatmentTypeRef> TreatmentTypeRefList = treatmentTypeRepository.getAllTreatmentsWithDesc(); 
		//return treatmentResponseMapper.createIllnessTreatment(treatmentData, treatmentTypeRepository,true);
		return treatmentResponseMapper.createIllnessTreatment(treatmentData, TreatmentTypeRefList,true);
	}

	@Override
	public void deleteSymptomBycode(String symptomID) {
		if(symptomID == null ){
			throw new DataInvalidException("SymptomID does not exists in the request");
		}



		symptomTreatmentRepository.deleteSymptomTmtDrugAndNDrugSources(symptomID.toUpperCase());
		symptomTreatmentRepository.deleteIllnessAllTreatments(symptomID.toUpperCase());
		symptomTreatmentRepository.deleteIllnessTreamentsGroups(symptomID.toUpperCase());
		symptomTreatmentRepository.deleteIllnessTreatmentsTmt(symptomID.toUpperCase());
		symptomTreatmentRepository.deleteSymptomTreatment(symptomID.toUpperCase());


	}

	@Override
	public void deleteTreatmentIllnessByCode(String icd10Code) {
		if(icd10Code == null  ){
			throw new DataInvalidException("ICD10Code does not exists in the request");
		}

		
		illnessTreatmentRespository.deleteIllnessTmtDrugPolicies(icd10Code.toUpperCase());
		illnessTreatmentRespository.deleteIllnessTmtDrugRxnorms(icd10Code.toUpperCase());
		illnessTreatmentRespository.deleteIllnessTmtDrugDosage(icd10Code.toUpperCase());
		illnessTreatmentRespository.deleteIllnessTmtDrugSources(icd10Code.toUpperCase());
		illnessTreatmentRespository.deleteIllnessTmtdrugsNonDrugs(icd10Code.toUpperCase());
		illnessTreatmentRespository.deleteIllnessTreamentsGroups(icd10Code.toUpperCase());
		illnessTreatmentRespository.deleteIllnessTreatmentsTmt(icd10Code.toUpperCase());
		illnessTreatmentRespository.deleteIllnessTreatment(icd10Code.toUpperCase());


	}

	@Override
	public TreatmentMainModel getTreatmentByIcd10CodeWithNoDesc(String icd10Code) {
	//	List<TreatmentTypeRef> TreatmentTypeRefList = treatmentTypeRepository.getAllTreatmentsWithDesc();
		List<TreatmentTypeRef> TreatmentTypeRefList = (List<TreatmentTypeRef>) treatmentTypeRepository.findAll();
		IllnessTreatment treatmentData = illnessTreatmentRespository.findTreatmentsByIcd10Code(icd10Code.toUpperCase());
		return treatmentResponseMapper.createIllnessTreatment(treatmentData, TreatmentTypeRefList,false);
	}

	@Override
	public TreatmentMainModel getTreatmentBySymptomIDWithNoDesc(String symptomID) {
//		List<TreatmentTypeRef> TreatmentTypeRefList = treatmentTypeRepository.getAllTreatmentsWithDesc();
			List<TreatmentTypeRef> TreatmentTypeRefList = (List<TreatmentTypeRef>) treatmentTypeRepository.findAll();
			SymptomTreatment dbSymptomTreatment = symptomTreatmentRepository.findTreatmentsBySymptomID(symptomID.toUpperCase());
		return	treatmentResponseMapper.createSymptomTreatment(dbSymptomTreatment,TreatmentTypeRefList,false);
	}

	@Override
	public TreatmentMainDocModel getTreatmentTypesByIcd10Code(String icd10Code) {
//		List<TreatmentTypeRef> TreatmentTypeRefList = treatmentTypeRepository.getAllTreatmentsWithDesc();
			List<TreatmentTypeRef> TreatmentTypeRefList = (List<TreatmentTypeRef>) treatmentTypeRepository.findAll();
		IllnessTreatment treatmentData = illnessTreatmentRespository.findTreatmentsByIcd10Code(icd10Code.toUpperCase());
		return treatmentResponseMapper.createIllnessDocTreatment(treatmentData, TreatmentTypeRefList);
	}

	@Override
	public TreatmentMainDocModel getTreatmentTypesBySymptomID(String symptomID) {
//		List<TreatmentTypeRef> TreatmentTypeRefList = treatmentTypeRepository.getAllTreatmentsWithDesc();
			List<TreatmentTypeRef> TreatmentTypeRefList = (List<TreatmentTypeRef>) treatmentTypeRepository.findAll();		
		SymptomTreatment dbSymptomTreatment = symptomTreatmentRepository.findTreatmentsBySymptomID(symptomID.toUpperCase());
		return	treatmentResponseMapper.createSymptomDocTreatment(dbSymptomTreatment,TreatmentTypeRefList);
	}

	@Override
	public List<TreatmentSourceInfo> getTreatmentSourcesForGivenIllness(String icd10Code) {
		return treatmentSourceInfoRepository.getTreatmentSourcesByIllness(icd10Code);
	}

	@Override
	public String updateTreatmentSources(TreatmentAuditSourcesModel treatmentSourcesModel) {

		Collection<Map<String, Object>> drugsSelf = new ArrayList<Map<String,Object>>();
		Collection<Map<String, Object>> drugsTreatment = new ArrayList<Map<String,Object>>();
		Collection<Map<String, Object>> drugsAll = new ArrayList<Map<String,Object>>();
		Collection<Map<String, Object>> drugsAddSources = new ArrayList<Map<String,Object>>();

		Collection<Map<String, Object>> nonDrugsSelf = new ArrayList<Map<String,Object>>();
		Collection<Map<String, Object>> nonDrugsTreatment = new ArrayList<Map<String,Object>>();
		Collection<Map<String, Object>> nonDrugsAll = new ArrayList<Map<String,Object>>();
		Collection<Map<String, Object>> nonDrugsAddSources = new ArrayList<Map<String,Object>>();

		String icd10Code = null;
		String symptomID = null;


		if(treatmentSourcesModel != null) {
			List<AuditTreatmentTypeModel> auditTreatmentTypes = treatmentSourcesModel.getTreatments();
			icd10Code =	treatmentSourcesModel.getIcd10Code();
			symptomID =	treatmentSourcesModel.getSymptomID();

			if( auditTreatmentTypes != null) {
				if(auditTreatmentTypes != null &&  !auditTreatmentTypes.isEmpty()) {
					for (AuditTreatmentTypeModel auditTreatmentTypeModel : auditTreatmentTypes) {
						if(auditTreatmentTypeModel != null ) {
							Integer typeDescID = auditTreatmentTypeModel.getTypeDescID();
							String drugName = auditTreatmentTypeModel.getDrugName();
							if(typeDescID != null ) {
								updateNonDrugSources(icd10Code,symptomID,auditTreatmentTypeModel, nonDrugsSelf, nonDrugsTreatment, nonDrugsAll, nonDrugsAddSources);
							}
							if(drugName != null) {
								updateDrugSources( icd10Code,symptomID, auditTreatmentTypeModel,  drugsSelf, drugsTreatment, drugsAll,  drugsAddSources);
							}

						}
					}

				}
			}
		}

		if(! drugsSelf.isEmpty() ) 
		{

			if(icd10Code != null) {
				treatmentSourceInfoRepository.unMapSourcesFromDrugs(drugsSelf);
			}

			if(symptomID != null) {
				treatmentSourceInfoRepository.unMapSourcesFromSymptomDrugs(drugsSelf);
			}

		}

		if(! drugsTreatment.isEmpty() ) 
		{
			if(icd10Code != null) {
				treatmentSourceInfoRepository.unMapSourcesFromDrugTreatment(drugsTreatment);
				treatmentSourceInfoRepository.unMapSourcesFromNonDrugTreatment(drugsTreatment);
			}
			if(symptomID != null) {
				treatmentSourceInfoRepository.unMapSourcesFromSymptomDrugTreatment(drugsTreatment);
				treatmentSourceInfoRepository.unMapSourcesFromSymptomsNonDrugTreatment(drugsTreatment);
			}

		}

		if(! drugsAll.isEmpty() ) 
		{

			treatmentSourceInfoRepository.unMapSourcesFromAllDrugSymptomTreatment(drugsAll);
			treatmentSourceInfoRepository.unMapSourcesFromAllNonDrugSymptomsTreatment(drugsAll);
			treatmentSourceInfoRepository.unMapSourcesFromAllDrugTreatment(drugsAll);
			treatmentSourceInfoRepository.unMapSourcesFromAllNonDrugTreatment(drugsAll);
		}


		if(! drugsAddSources.isEmpty() ) 
		{
			if(icd10Code != null) {
				treatmentSourceInfoRepository.mapSourcesToDrugs(drugsAddSources);
			}
			if(symptomID != null) { 
				treatmentSourceInfoRepository.mapSourcesToDrugSymptoms(drugsAddSources);

			}
		}

		if(! nonDrugsSelf.isEmpty() ) 
		{
			if(icd10Code != null) {
				treatmentSourceInfoRepository.unMapSourcesFromNonDrugs(nonDrugsSelf);
			}
			if(symptomID != null) {
				treatmentSourceInfoRepository.unMapSourcesFromNonDrugSymptoms(nonDrugsSelf);

			}

		}

		if(! nonDrugsTreatment.isEmpty() ) 
		{

			if(icd10Code != null) {
				treatmentSourceInfoRepository.unMapSourcesFromDrugTreatment(nonDrugsTreatment);
				treatmentSourceInfoRepository.unMapSourcesFromNonDrugTreatment(nonDrugsTreatment);
			}
			if(symptomID != null) {
				treatmentSourceInfoRepository.unMapSourcesFromSymptomDrugTreatment(nonDrugsTreatment);
				treatmentSourceInfoRepository.unMapSourcesFromSymptomsNonDrugTreatment(nonDrugsTreatment);
			}
		}

		if(! nonDrugsAll.isEmpty() ) 
		{
			treatmentSourceInfoRepository.unMapSourcesFromAllNonDrugTreatment(nonDrugsAll);
			treatmentSourceInfoRepository.unMapSourcesFromAllDrugTreatment(nonDrugsAll);			
			treatmentSourceInfoRepository.unMapSourcesFromAllDrugSymptomTreatment(nonDrugsAll);
			treatmentSourceInfoRepository.unMapSourcesFromAllNonDrugSymptomsTreatment(nonDrugsAll);



		}
		if(! nonDrugsAddSources.isEmpty() ) 
		{
			if(icd10Code != null) {
				treatmentSourceInfoRepository.mapSourcesToNonDrugs(nonDrugsAddSources);
			}
			if(symptomID != null) { 
				treatmentSourceInfoRepository.mapSourcesToNonDrugsSymptoms(nonDrugsAddSources);

			}
		}


		return "Treatment sources updated";
	}



	private void updateNonDrugSources(String icd10Code,String symptomID,AuditTreatmentTypeModel auditTreatmentTypeModel, Collection<Map<String, Object>> nonDrugsSelf,Collection<Map<String, Object>> nonDrugsTreatment,Collection<Map<String, Object>> nonDrugsAll, Collection<Map<String, Object>> nonDrugsAddSources) {
		List<AuditSourceModel> sourceList = auditTreatmentTypeModel.getSourceInfo();		
		if(sourceList != null &&  !sourceList.isEmpty()) {
			for (AuditSourceModel auditSourceModel : sourceList) {
				if( auditSourceModel != null ) {
					Integer sourceId = auditSourceModel.getSourceID() ;
					String action = auditSourceModel.getAction();
					Integer typeDescID = auditTreatmentTypeModel.getTypeDescID();									
					if(sourceId !=null && action != null) {
						Map<String, Object> sourceInfo = new Hashtable<String, Object>();

						if(icd10Code != null) {
							sourceInfo.put("icd10Code", icd10Code);
						}

						if(symptomID != null) { 
							sourceInfo.put("symptomID", symptomID);
						}

						sourceInfo.put("typeDescID",typeDescID) ;
						sourceInfo.put("sourceID", auditSourceModel.getSourceID());	
						if(auditSourceModel.getAction().equalsIgnoreCase("Drug/NonDrug") ){
							sourceInfo.put("action", "Unmap");
							nonDrugsSelf.add(sourceInfo);
						} 
						if(auditSourceModel.getAction().equalsIgnoreCase("Treatment")){
							sourceInfo.put("action", "Unmap");
							nonDrugsTreatment.add(sourceInfo);
						}
						if(auditSourceModel.getAction().equalsIgnoreCase("All")){
							sourceInfo.put("action", "Unmap/Deleted");
							nonDrugsAll.add(sourceInfo);
						}

						if(auditSourceModel.getAction().equalsIgnoreCase("Add")){
							sourceInfo.put("action", "Added");
							nonDrugsAddSources.add(sourceInfo);

						}

					}

				}
			}

		}

	}


	private void updateDrugSources(String icd10Code, String symptomID,  AuditTreatmentTypeModel auditTreatmentTypeModel, Collection<Map<String, Object>> drugsSelf,Collection<Map<String, Object>> drugsTreatment,Collection<Map<String, Object>> drugsAll, Collection<Map<String, Object>> drugsAddSources) {
		List<AuditSourceModel> sourceList = auditTreatmentTypeModel.getSourceInfo();		
		if(sourceList != null &&  !sourceList.isEmpty()) {
			for (AuditSourceModel auditSourceModel : sourceList) {
				if( auditSourceModel != null ) {
					Integer sourceId = auditSourceModel.getSourceID() ;
					String action = auditSourceModel.getAction();
					String drugName = auditTreatmentTypeModel.getDrugName();
					if(sourceId !=null && action != null) {						
						Map<String, Object> sourceInfo = new Hashtable<String, Object>();

						if(icd10Code != null) { 
							sourceInfo.put("icd10Code", icd10Code);
						}

						if(symptomID != null) { 
							sourceInfo.put("symptomID", symptomID);
						}

						sourceInfo.put("drug",drugName) ;
						sourceInfo.put("sourceID", auditSourceModel.getSourceID());	
						if(auditSourceModel.getAction().equalsIgnoreCase("Drug/NonDrug") ){
							sourceInfo.put("action", "Unmap");
							drugsSelf.add(sourceInfo);
						} 
						if(auditSourceModel.getAction().equalsIgnoreCase("Treatment")){
							sourceInfo.put("action", "Unmap");
							drugsTreatment.add(sourceInfo);
						}
						if(auditSourceModel.getAction().equalsIgnoreCase("All")){
							sourceInfo.put("action", "Unmap/Deleted");
							drugsAll.add(sourceInfo);
						}

						if(auditSourceModel.getAction().equalsIgnoreCase("Add")){
							sourceInfo.put("action", "Added");
							drugsAddSources.add(sourceInfo);

						}

					}

				}
			}

		}


	}

	@Override
	public List<TreatmentSourceInfo> getTreatmentSourcesForGivenSymptom(
			String symptomID) {
    	return treatmentSourceInfoRepository.getTreatmentSourcesBySymptom(symptomID);
	}



}
