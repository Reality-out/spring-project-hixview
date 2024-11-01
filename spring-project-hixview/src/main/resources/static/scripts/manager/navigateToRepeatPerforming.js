document.addEventListener('DOMContentLoaded', () => {
    console.log('navigateToRepeatPerforming started')
    
    const repeatForm = document.querySelector('.form-repeat-page');
    repeatForm.addEventListener('submit', function(event) {
        event.preventDefault();
        window.location = repeatForm.dataset.repeatPath;
    });
})