public interface ICashRequestRepository {
    List<CashRequest> getAll();
    List<CashRequest> getByMandoob(int mandoobId);
    List<CashRequest> getPending();
    String getStatus(int id);
    int count();
    boolean create(CashRequest cashRequest);
    boolean approve(int id, int adminId);
    boolean reject(int id, int adminId, String notes);
}
