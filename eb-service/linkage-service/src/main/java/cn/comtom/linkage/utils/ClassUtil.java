
package cn.comtom.linkage.utils;

public class ClassUtil {
	
	public static Class<?>[] ebdClasses=new Class[]{
		cn.comtom.linkage.main.access.model.ebd.EBD.class, cn.comtom.linkage.main.access.model.ebd.ebm.SRC.class, cn.comtom.linkage.main.access.model.ebd.ebm.DEST.class, cn.comtom.linkage.main.access.model.ebd.ebm.RelatedEBD.class,
		cn.comtom.linkage.main.access.model.ebd.details.other.EBM.class, cn.comtom.linkage.main.access.model.ebd.ebm.RelatedInfo.class, cn.comtom.linkage.main.access.model.ebd.ebm.MsgBasicInfo.class, cn.comtom.linkage.main.access.model.ebd.ebm.MsgContent.class, cn.comtom.linkage.main.access.model.ebd.ebm.Auxiliary.class,
			cn.comtom.linkage.main.access.model.ebd.ebm.Dispatch.class, cn.comtom.linkage.main.access.model.ebd.commom.EBRPS.class, cn.comtom.linkage.main.access.model.ebd.commom.EBRAS.class, cn.comtom.linkage.main.access.model.ebd.commom.EBRBS.class,
				/*可删除*/cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRPS.class, cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRST.class, cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRAS.class, cn.comtom.linkage.main.access.model.ebd.commom.Coverage.class,
				/*可删除*/cn.comtom.linkage.main.access.model.ebd.commom.RadioParams.class, cn.comtom.linkage.main.access.model.ebd.commom.TVParams.class, cn.comtom.linkage.main.access.model.ebd.commom.Schedule.class, cn.comtom.linkage.main.access.model.ebd.commom.Switch.class,
				
		cn.comtom.linkage.main.access.model.ebd.details.other.EBMStateResponse.class, cn.comtom.linkage.main.access.model.ebd.details.other.EBM.class, cn.comtom.linkage.main.access.model.ebd.commom.Coverage.class, cn.comtom.linkage.main.access.model.ebd.ebm.ResBrdInfo.class, cn.comtom.linkage.main.access.model.ebd.ebm.ResBrdItem.class,
			cn.comtom.linkage.main.access.model.ebd.commom.EBRPS.class, cn.comtom.linkage.main.access.model.ebd.commom.EBRST.class, cn.comtom.linkage.main.access.model.ebd.commom.EBRAS.class, cn.comtom.linkage.main.access.model.ebd.commom.EBRBS.class,
				/*可删除*/cn.comtom.linkage.main.access.model.ebd.ebm.RelatedInfo.class, cn.comtom.linkage.main.access.model.ebd.ebm.MsgBasicInfo.class, cn.comtom.linkage.main.access.model.ebd.ebm.MsgContent.class, cn.comtom.linkage.main.access.model.ebd.ebm.Auxiliary.class,
				/*可删除*/cn.comtom.linkage.main.access.model.ebd.ebm.Dispatch.class, cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRPS.class, cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRST.class, cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRAS.class,
				/*可删除*/cn.comtom.linkage.main.access.model.ebd.commom.RadioParams.class, cn.comtom.linkage.main.access.model.ebd.commom.TVParams.class, cn.comtom.linkage.main.access.model.ebd.commom.Schedule.class, cn.comtom.linkage.main.access.model.ebd.commom.Switch.class,
			
		cn.comtom.linkage.main.access.model.ebd.details.other.EBMStateRequest.class, cn.comtom.linkage.main.access.model.ebd.details.other.EBM.class,
				/*可删除*/cn.comtom.linkage.main.access.model.ebd.ebm.RelatedInfo.class, cn.comtom.linkage.main.access.model.ebd.ebm.MsgBasicInfo.class, cn.comtom.linkage.main.access.model.ebd.ebm.MsgContent.class, cn.comtom.linkage.main.access.model.ebd.ebm.Auxiliary.class,
				/*可删除*/cn.comtom.linkage.main.access.model.ebd.ebm.Dispatch.class, cn.comtom.linkage.main.access.model.ebd.commom.EBRPS.class, cn.comtom.linkage.main.access.model.ebd.commom.EBRAS.class, cn.comtom.linkage.main.access.model.ebd.commom.EBRBS.class,
				/*可删除*/cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRPS.class, cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRST.class, cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRAS.class, cn.comtom.linkage.main.access.model.ebd.commom.Coverage.class,
				/*可删除*/cn.comtom.linkage.main.access.model.ebd.commom.RadioParams.class, cn.comtom.linkage.main.access.model.ebd.commom.TVParams.class, cn.comtom.linkage.main.access.model.ebd.commom.Schedule.class, cn.comtom.linkage.main.access.model.ebd.commom.Switch.class,
			
		cn.comtom.linkage.main.access.model.ebd.details.other.OMDRequest.class, cn.comtom.linkage.main.access.model.ebd.commom.Params.class,
		cn.comtom.linkage.main.access.model.ebd.details.info.EBRPSInfo.class, cn.comtom.linkage.main.access.model.ebd.commom.Params.class, cn.comtom.linkage.main.access.model.ebd.commom.EBRPS.class, cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRPS.class,
		cn.comtom.linkage.main.access.model.ebd.details.info.EBRSTInfo.class, cn.comtom.linkage.main.access.model.ebd.commom.Params.class, cn.comtom.linkage.main.access.model.ebd.commom.EBRST.class, cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRPS.class,
		cn.comtom.linkage.main.access.model.ebd.details.info.EBRASInfo.class, cn.comtom.linkage.main.access.model.ebd.commom.Params.class, cn.comtom.linkage.main.access.model.ebd.commom.EBRAS.class, cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRPS.class, cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRST.class,
		cn.comtom.linkage.main.access.model.ebd.details.info.EBRDTInfo.class, cn.comtom.linkage.main.access.model.ebd.commom.Params.class, cn.comtom.linkage.main.access.model.ebd.commom.EBRDT.class, cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRPS.class,
		cn.comtom.linkage.main.access.model.ebd.details.info.EBRBSInfo.class, cn.comtom.linkage.main.access.model.ebd.commom.Params.class, cn.comtom.linkage.main.access.model.ebd.commom.EBRBS.class, cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRPS.class, cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRST.class, cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRAS.class,
				/*可删除*/cn.comtom.linkage.main.access.model.ebd.commom.Coverage.class, cn.comtom.linkage.main.access.model.ebd.commom.RadioParams.class, cn.comtom.linkage.main.access.model.ebd.commom.TVParams.class, cn.comtom.linkage.main.access.model.ebd.commom.Schedule.class, cn.comtom.linkage.main.access.model.ebd.commom.Switch.class,
			
		cn.comtom.linkage.main.access.model.ebd.details.other.EBMBrdLog.class, cn.comtom.linkage.main.access.model.ebd.commom.Params.class, cn.comtom.linkage.main.access.model.ebd.ebm.EBMBrdItem.class, cn.comtom.linkage.main.access.model.ebd.details.other.EBM.class, cn.comtom.linkage.main.access.model.ebd.ebm.MsgBasicInfo.class, cn.comtom.linkage.main.access.model.ebd.ebm.MsgContent.class,
			cn.comtom.linkage.main.access.model.ebd.ebm.UnitInfo.class, cn.comtom.linkage.main.access.model.ebd.ebm.Unit.class, cn.comtom.linkage.main.access.model.ebd.commom.EBRPS.class,
				/*可删除*/cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRPS.class, cn.comtom.linkage.main.access.model.ebd.ebm.Auxiliary.class, cn.comtom.linkage.main.access.model.ebd.ebm.RelatedInfo.class, cn.comtom.linkage.main.access.model.ebd.ebm.Dispatch.class,
				/*可删除*/cn.comtom.linkage.main.access.model.ebd.commom.EBRPS.class, cn.comtom.linkage.main.access.model.ebd.commom.EBRAS.class, cn.comtom.linkage.main.access.model.ebd.commom.EBRBS.class,
				/*可删除*/cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRPS.class, cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRST.class, cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRAS.class, cn.comtom.linkage.main.access.model.ebd.commom.Coverage.class,
				/*可删除*/cn.comtom.linkage.main.access.model.ebd.commom.RadioParams.class, cn.comtom.linkage.main.access.model.ebd.commom.TVParams.class, cn.comtom.linkage.main.access.model.ebd.commom.Schedule.class, cn.comtom.linkage.main.access.model.ebd.commom.Switch.class,
		cn.comtom.linkage.main.access.model.ebd.details.state.EBRPSState.class, cn.comtom.linkage.main.access.model.ebd.commom.EBRPS.class,  /*可删除*/cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRPS.class,
		cn.comtom.linkage.main.access.model.ebd.details.state.EBRASState.class, cn.comtom.linkage.main.access.model.ebd.commom.EBRAS.class,  /*可删除*/cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRPS.class, cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRST.class,
		cn.comtom.linkage.main.access.model.ebd.details.state.EBRBSState.class, cn.comtom.linkage.main.access.model.ebd.commom.EBRBS.class,  /*可删除*/cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRPS.class, cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRST.class, cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRAS.class,
		cn.comtom.linkage.main.access.model.ebd.details.state.EBRDTState.class, cn.comtom.linkage.main.access.model.ebd.commom.EBRDT.class,  /*可删除*/cn.comtom.linkage.main.access.model.ebd.commom.RelatedEBRPS.class,
		cn.comtom.linkage.main.access.model.ebd.details.other.ConnectionCheck.class,
		cn.comtom.linkage.main.access.model.ebd.details.other.EBDResponse.class
	};
	
	public static Class<?>[] sigClasses=new Class[]{
		cn.comtom.linkage.main.access.model.signature.Signature.class, cn.comtom.linkage.main.access.model.ebd.ebm.RelatedEBD.class, cn.comtom.linkage.main.access.model.signature.SignatureCert.class,
		cn.comtom.linkage.main.access.model.ebd.ebm.RelatedEBInfo.class,cn.comtom.linkage.main.access.model.ebd.ebm.EBInfoID.class
	};
}
