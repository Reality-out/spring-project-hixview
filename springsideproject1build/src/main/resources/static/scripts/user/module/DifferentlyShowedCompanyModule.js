(function() {
    function adjustGridLayout() {
        let bottomComponent = document.getElementById('bottomComponent');
        let bottomLeftComponent = document.getElementById('bottomLeftComponent');
        let bottomRightComponent = document.getElementById('bottomRightComponent');
        let bottomLeftAllLineComponents = document.querySelector('#bottomLeftComponent > nl');
        let bottomLeftOneLineComponents = document.querySelectorAll('#bottomLeftComponent > nl > nl');

        let viewportWidth = window.innerWidth;

        bottomComponent.classList.remove('long', 'middle', 'short');
        bottomLeftComponent.classList.remove('longMiddle', 'short');
        bottomRightComponent.classList.remove('common');
        bottomLeftAllLineComponents.classList.remove('longMiddle', 'short');
        bottomLeftOneLineComponents.forEach(component => component.classList.remove('longMiddle', 'short'));

        if (viewportWidth >= 1600) {
            bottomComponent.classList.add('long');
            bottomLeftComponent.classList.add('longMiddle');
            bottomLeftAllLineComponents.classList.add('longMiddle');
            bottomLeftOneLineComponents.forEach(component => component.classList.add('longMiddle'));

        } else if (viewportWidth >= 800) {
            bottomComponent.classList.add('middle');
            bottomLeftComponent.classList.add('longMiddle');
            bottomLeftAllLineComponents.classList.add('longMiddle');
            bottomLeftOneLineComponents.forEach(component => component.classList.add('longMiddle'));
        } else {
            bottomComponent.classList.add('short');
            bottomLeftComponent.classList.add('short');
            bottomLeftAllLineComponents.classList.add('short');
            bottomLeftOneLineComponents.forEach(component => component.classList.add('short'));
        }

        bottomRightComponent.classList.add('common');
    }

    window.adjustGridLayout = adjustGridLayout;
})();