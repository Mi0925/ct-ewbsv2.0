package cn.comtom.linkage.main.ebr.service;

/**
 * @author:WJ
 * @date: 2019/3/5 0005
 * @time: 下午 3:21
 */
public interface IEbrSyncService {
    void psInfoReport();

    void psStateReport();

    void bsInfoReport();

    void bsStateReport();

    void dtInfoReport();

    void dtStateReport();

    void brdLogReport();

	void stInfoReport();

	void asInfoReport();

	void asStateReport();
}
