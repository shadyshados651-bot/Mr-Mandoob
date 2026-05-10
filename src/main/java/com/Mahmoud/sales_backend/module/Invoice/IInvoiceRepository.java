package com.Mahmoud.sales_backend.module.Invoice;

import com.Mahmoud.sales_backend.model.Invoice;
import com.Mahmoud.sales_backend.model.InvoiceItem;
import java.util.List;

public interface IInvoiceRepository {
    List<Invoice> getAll();
    Invoice getById(int id);
    List<Invoice> getInvoicesByClient(int clientId);
    List<Invoice> getInvoicesByMandoob(int mandoobId);
    int getTodayInvoicesCount();
    List<InvoiceItem> getInvoiceItems(int invoiceId);
    int count();
    boolean create(Invoice invoice);
    boolean delete(int id);
    double getTotalDebt();
    double getMandoobTotalDebt(int mandoobId);
}
