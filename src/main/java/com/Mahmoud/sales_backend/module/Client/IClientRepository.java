package com.Mahmoud.sales_backend.module.Client;

import com.Mahmoud.sales_backend.model.Client;
import java.util.List;

public interface IClientRepository {
    List<Client> getAll();
    List<Client> getAllWithDebt();
    List<Client> getClientsByMandoobWithDebt(int mandoobId);
    List<Client> getClientsByMandoob(int mandoobid);
    Client getById(int id);
    boolean create(Client client);
    boolean delete(int id);
}
