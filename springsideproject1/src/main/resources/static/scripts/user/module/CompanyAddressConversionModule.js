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
        const codeOrName = document.getElementById('codeOrName').value;
        const url = "/company/search/" + encodeURIComponent(codeOrName);
        window.location.href = url;
    }
}

export default CompanyAddressConversionModule;