package cn.comtom.core.main.sichuan.service;

import java.io.File;

import org.apache.commons.digester3.Digester;

import cn.comtom.core.main.sichuan.Ebm;
import cn.comtom.core.main.sichuan.dao.SichuanXMLDao;
import cn.comtom.core.main.sichuan.entity.Auxiliary;
import cn.comtom.core.main.sichuan.entity.Content;
import cn.comtom.core.main.sichuan.entity.Dispatch;
import cn.comtom.core.main.sichuan.entity.EBSNotice;
import cn.comtom.core.main.sichuan.entity.EmergencyInfo;
import cn.comtom.core.main.sichuan.entity.SysID;

public class SichuanEBMXML implements SichuanXMLDao {
	
	public SichuanEBMXML() {
		
	}
	

	@Override
	public Ebm analyze(File filePath) {
		
		File ebmXML = filePath;
		Ebm ebm = null;// 经解析得到的EBM类型的XML文件对象
		EBSNotice eBSNotice = null;
		
		System.out.println("analyze 1 = "+ebmXML.getName());
		
		// 建立一个Digester对象
		Digester digester = new Digester();
		// 指定它不要用DTD验证XML文档的合法性——这是因为我们没有为XML文档定义DTD
		digester.setValidating(false);
		
		// 设置XML文件解析规则——头部解析规则
		
		
		digester.addObjectCreate("soapenv:Envelope/soapenv:Body/EBSNotice", EBSNotice.class);
		
		digester.addObjectCreate("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info", EmergencyInfo.class);
		digester.addSetNext("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info", "setEmergencyInfo");
		
		// 从EBM标签开始解析,并新建一个EBM对象做为根对象
		digester.addObjectCreate("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM", Ebm.class);
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/platformID", "platformID");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/msgID", "msgID");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/processType", "processType");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/sender", "sender");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/targetPlatformID", "targetPlatformID");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/drillType", "drillType");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/eventType", "eventType");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/severity", "severity");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/sendTime", "sendTime");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/endTime", "endTime");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/duration", "duration");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/geocode", "geocode");
		digester.addSetNext("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM", "setEbm");
		//
		digester.addObjectCreate("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/dispatch", Dispatch.class);
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/dispatch/receiverID", "receiverID");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/dispatch/sysType", "sysType");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/dispatch/presentation", "presentation");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/dispatch/rollFrequency", "rollFrequency");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/dispatch/returnWay", "returnWay");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/dispatch/returnTel", "returnTel");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/dispatch/returnIP", "returnIP");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/dispatch/returnPort", "returnPort");
		digester.addSetNext("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/dispatch", "setDispatch");
		//
		digester.addObjectCreate("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/dispatch/sysID", SysID.class);
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/dispatch/sysID/resurse_id", "resurse_id");
		digester.addSetNext("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/dispatch/sysID", "setSysID");
		//
		digester.addObjectCreate("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/content", Content.class);
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/content/languageCod", "languageCod");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/content/codeSet", "codeSet");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/content/description", "description");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/content/programNum", "programNum");
		digester.addSetNext("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/content", "setContent");
		
		digester.addObjectCreate("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/content/auxiliary", Auxiliary.class);
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/content/auxiliary/auxiliaryType", "auxiliaryType");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/content/auxiliary/repeatNum", "repeatNum");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/content/auxiliary/auxiliaryBody", "auxiliaryBody");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/content/auxiliary/size", "size");
		digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/content/auxiliary/resourceDesc", "resourceDesc");
		digester.addSetNext("soapenv:Envelope/soapenv:Body/EBSNotice/Emergency_Info/EBM/content/auxiliary", "setAuxiliary");
		System.out.println("analyze 2");
		
		try {
			if(null != ebmXML){
				System.out.println("analyze 3");
				eBSNotice = (EBSNotice)digester.parse(ebmXML);// 第一次解析XML文件，获取数据包类型
				System.out.println("ProcessType = "+eBSNotice.getEmergencyInfo().getEbm().getProcessType());
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("analyze 4");
		}
		//System.out.println(ebm.getProcessType());
		return eBSNotice.getEmergencyInfo().getEbm();
	}

}
