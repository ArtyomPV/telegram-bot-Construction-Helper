package ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ContractWithTotalPaymentsDTO {
    private Long id;
    private String contractNumber;
    private String description;
    private String customerName;
    private String contractorName;
    private BigDecimal contractAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isCompleted;
    private BigDecimal totalPayments;

    public ContractWithTotalPaymentsDTO(Long id,
                                        String contractNumber,
                                        String description,
                                        String customerName,
                                        String contractorName,
                                        BigDecimal contractAmount,
                                        LocalDate startDate,
                                        LocalDate endDate,
                                        Boolean isCompleted,
                                        BigDecimal totalPayments) {
        this.id = id;
        this.contractNumber = contractNumber;
        this.description = description;
        this.customerName = customerName;
        this.contractorName = contractorName;
        this.contractAmount = contractAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCompleted = isCompleted;
        this.totalPayments = totalPayments;
    }

    public static ContractWithTotalPaymentsDTOBuilder builder() {
        return new ContractWithTotalPaymentsDTOBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public String getContractNumber() {
        return this.contractNumber;
    }

    public String getDescription() {
        return this.description;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public String getContractorName() {
        return this.contractorName;
    }

    public BigDecimal getContractAmount() {
        return this.contractAmount;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Boolean getIsCompleted() {
        return this.isCompleted;
    }

    public BigDecimal getTotalPayments() {
        return this.totalPayments;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }

    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public void setTotalPayments(BigDecimal totalPayments) {
        this.totalPayments = totalPayments;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ContractWithTotalPaymentsDTO)) return false;
        final ContractWithTotalPaymentsDTO other = (ContractWithTotalPaymentsDTO) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$contractNumber = this.getContractNumber();
        final Object other$contractNumber = other.getContractNumber();
        if (this$contractNumber == null ? other$contractNumber != null : !this$contractNumber.equals(other$contractNumber))
            return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description))
            return false;
        final Object this$customerName = this.getCustomerName();
        final Object other$customerName = other.getCustomerName();
        if (this$customerName == null ? other$customerName != null : !this$customerName.equals(other$customerName))
            return false;
        final Object this$contractorName = this.getContractorName();
        final Object other$contractorName = other.getContractorName();
        if (this$contractorName == null ? other$contractorName != null : !this$contractorName.equals(other$contractorName))
            return false;
        final Object this$contractAmount = this.getContractAmount();
        final Object other$contractAmount = other.getContractAmount();
        if (this$contractAmount == null ? other$contractAmount != null : !this$contractAmount.equals(other$contractAmount))
            return false;
        final Object this$startDate = this.getStartDate();
        final Object other$startDate = other.getStartDate();
        if (this$startDate == null ? other$startDate != null : !this$startDate.equals(other$startDate)) return false;
        final Object this$endDate = this.getEndDate();
        final Object other$endDate = other.getEndDate();
        if (this$endDate == null ? other$endDate != null : !this$endDate.equals(other$endDate)) return false;
        final Object this$isCompleted = this.getIsCompleted();
        final Object other$isCompleted = other.getIsCompleted();
        if (this$isCompleted == null ? other$isCompleted != null : !this$isCompleted.equals(other$isCompleted))
            return false;
        final Object this$totalPayments = this.getTotalPayments();
        final Object other$totalPayments = other.getTotalPayments();
        if (this$totalPayments == null ? other$totalPayments != null : !this$totalPayments.equals(other$totalPayments))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ContractWithTotalPaymentsDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $contractNumber = this.getContractNumber();
        result = result * PRIME + ($contractNumber == null ? 43 : $contractNumber.hashCode());
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        final Object $customerName = this.getCustomerName();
        result = result * PRIME + ($customerName == null ? 43 : $customerName.hashCode());
        final Object $contractorName = this.getContractorName();
        result = result * PRIME + ($contractorName == null ? 43 : $contractorName.hashCode());
        final Object $contractAmount = this.getContractAmount();
        result = result * PRIME + ($contractAmount == null ? 43 : $contractAmount.hashCode());
        final Object $startDate = this.getStartDate();
        result = result * PRIME + ($startDate == null ? 43 : $startDate.hashCode());
        final Object $endDate = this.getEndDate();
        result = result * PRIME + ($endDate == null ? 43 : $endDate.hashCode());
        final Object $isCompleted = this.getIsCompleted();
        result = result * PRIME + ($isCompleted == null ? 43 : $isCompleted.hashCode());
        final Object $totalPayments = this.getTotalPayments();
        result = result * PRIME + ($totalPayments == null ? 43 : $totalPayments.hashCode());
        return result;
    }

    public String toString() {
        return "ContractWithTotalPaymentsDTO(id=" + this.getId() + ", contractNumber=" + this.getContractNumber() + ", description=" + this.getDescription() + ", customerName=" + this.getCustomerName() + ", contractorName=" + this.getContractorName() + ", contractAmount=" + this.getContractAmount() + ", startDate=" + this.getStartDate() + ", endDate=" + this.getEndDate() + ", isCompleted=" + this.getIsCompleted() + ", totalPayments=" + this.getTotalPayments() + ")";
    }

    public static class ContractWithTotalPaymentsDTOBuilder {
        private Long id;
        private String contractNumber;
        private String description;
        private String customerName;
        private String contractorName;
        private BigDecimal contractAmount;
        private LocalDate startDate;
        private LocalDate endDate;
        private Boolean isCompleted;
        private BigDecimal totalPayments;

        ContractWithTotalPaymentsDTOBuilder() {
        }

        public ContractWithTotalPaymentsDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ContractWithTotalPaymentsDTOBuilder contractNumber(String contractNumber) {
            this.contractNumber = contractNumber;
            return this;
        }

        public ContractWithTotalPaymentsDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ContractWithTotalPaymentsDTOBuilder customerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public ContractWithTotalPaymentsDTOBuilder contractorName(String contractorName) {
            this.contractorName = contractorName;
            return this;
        }

        public ContractWithTotalPaymentsDTOBuilder contractAmount(BigDecimal contractAmount) {
            this.contractAmount = contractAmount;
            return this;
        }

        public ContractWithTotalPaymentsDTOBuilder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public ContractWithTotalPaymentsDTOBuilder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public ContractWithTotalPaymentsDTOBuilder isCompleted(Boolean isCompleted) {
            this.isCompleted = isCompleted;
            return this;
        }

        public ContractWithTotalPaymentsDTOBuilder totalPayments(BigDecimal totalPayments) {
            this.totalPayments = totalPayments;
            return this;
        }

        public ContractWithTotalPaymentsDTO build() {
            return new ContractWithTotalPaymentsDTO(this.id, this.contractNumber, this.description, this.customerName, this.contractorName, this.contractAmount, this.startDate, this.endDate, this.isCompleted, this.totalPayments);
        }

        public String toString() {
            return "ContractWithTotalPaymentsDTO.ContractWithTotalPaymentsDTOBuilder(id=" + this.id + ", contractNumber=" + this.contractNumber + ", description=" + this.description + ", customerName=" + this.customerName + ", contractorName=" + this.contractorName + ", contractAmount=" + this.contractAmount + ", startDate=" + this.startDate + ", endDate=" + this.endDate + ", isCompleted=" + this.isCompleted + ", totalPayments=" + this.totalPayments + ")";
        }
    }
}
