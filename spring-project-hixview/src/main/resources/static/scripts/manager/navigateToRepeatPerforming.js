document.addEventListener('DOMContentLoaded', () => {
    const repeatForm = document.querySelector('.form-repeat-page');
    repeatForm.addEventListener('submit', function(event) {
        event.preventDefault();
        window.location = repeatForm.dataset.repeatPath;
    });
})