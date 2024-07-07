(function() {
    function adjustGridLayout() {
        let bottomComponent = document.getElementById('bottomComponent');
        let bottomLeftComponent = document.getElementById('bottomLeftComponent');
        let bottomRightComponent = document.getElementById('bottomRightComponent');

        let viewportWidth = window.innerWidth;

        if (viewportWidth >= 1600) {
            bottomComponent.classList.remove('middle');
            bottomComponent.classList.add('long');
            bottomLeftComponent.classList.add('common');
            bottomRightComponent.classList.add('common');
        } else {
            bottomComponent.classList.remove('long');
            bottomComponent.classList.add('middle');
            bottomLeftComponent.classList.add('common');
            bottomRightComponent.classList.add('common');
        }
    }

    window.adjustGridLayout = adjustGridLayout;
})();