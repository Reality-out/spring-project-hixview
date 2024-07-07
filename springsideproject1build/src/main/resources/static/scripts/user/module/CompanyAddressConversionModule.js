class CompanyAddressConversionModule {
    constructor() {
        this.init();
    }

    init() {
        this.attachSubmitHandler();
    }

    attachSubmitHandler() {
        document.getElementById('searchCompanyForm').addEventListener('submit', this.handleSubmit.bind(this));
    }

    handleSubmit(event) {
        event.preventDefault();
        const nameOrCode = document.getElementById('nameOrCode').value;
        const url = "/company/companies/" + encodeURIComponent(nameOrCode);
        window.location.href = url;
    }
}

export default CompanyAddressConversionModule;