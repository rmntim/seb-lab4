document.getElementById('click_catcher').addEventListener('mousemove', evt => {
    let target = evt.target;

    let offset = getCoords(evt.target);
    const width = offset.right - offset.left;
    const height = offset.bottom - offset.top;

    evt.target = target;
    let x = (evt.pageX - offset.left - width / 2) / (width / 2) * (5 / 2 * 3);
    let y = -((evt.pageY - offset.top - height / 2) / (height / 2) * (5 / 2 * 3));

    drawOMarker(x, y);

    document.getElementById("hidden-form:graph-x").value = x;
    document.getElementById("hidden-form:graph-y").value = y;
    document.getElementById("hidden-form:graph-r").value = getR();
});

document.getElementById('click_catcher').addEventListener('mouseleave', deleteOMarker);

document.getElementById('click_catcher').addEventListener('click', () => {
    document.getElementById("hidden-form:graph-send").click();
});

function handleSlide(event) {
    redrawFigure(getR());
}

function getR() {
    const r = PF("widget_main_form_superSlider").getValue();
    return r;
}

function getCoords(elem) {
    let box = elem.getBoundingClientRect();

    return {
        top: box.top + window.pageYOffset,
        right: box.right + window.pageXOffset,
        bottom: box.bottom + window.pageYOffset,
        left: box.left + window.pageXOffset
    };
}

function drawOMarker(x, y) {
    deleteOMarker();
    const svg = document.getElementById('graph');

    let circle = document.createElementNS("http://www.w3.org/2000/svg", 'circle');
    circle.setAttributeNS(null, 'cx', ((x) * 20 + 150).toString());
    circle.setAttributeNS(null, 'cy', (-y * 20 + 150).toString());
    circle.setAttributeNS(null, 'r', '8');
    circle.setAttributeNS(null, 'stroke', 'rgb(174, 193, 187)');
    circle.setAttributeNS(null, 'stroke-width', '5');
    circle.setAttributeNS(null, 'fill-opacity', '0');
    circle.id = "selected_pos";
    svg.appendChild(circle);
}

function deleteOMarker() {
    let circle = document.getElementById('selected_pos');
    if (circle !== null) circle.parentElement.removeChild(circle);
}

function drawDot(x, y, checked) {
    const svg = document.getElementById('graph');

    let circle = document.createElementNS("http://www.w3.org/2000/svg", 'circle');
    circle.setAttributeNS(null, 'cx', ((x) * 20 + 150).toString());
    circle.setAttributeNS(null, 'cy', (-y * 20 + 150).toString());
    circle.setAttributeNS(null, 'r', '4');
    circle.classList.add("littleDot");

    if (checked) {
        circle.setAttributeNS(null, 'fill', 'mediumspringgreen');
    } else {
        circle.setAttributeNS(null, 'fill', 'palevioletred');
    }
    svg.appendChild(circle);
}

function redrawFigure(scale) {
    let figure1 = document.getElementById("figure1");
    figure1.setAttributeNS(null, 'points', (150 - 20 * scale) + ",150 150,150 150," + (150 - 10 * scale));

    let figure2 = document.getElementById("figure2");
    figure2.setAttributeNS(null, 'points', (150 + 20 * scale) + ",150 " + (150 + 20 * scale) + "," + (150 - 10 * scale) + " 150," + (150 - 10 * scale) + " 150,150");

    let figure3 = document.getElementById("figure3");
    figure3.setAttributeNS(null, 'd', "M 150 150 L " + (150 + 10 * scale) + " 150 A " + (10 * scale) + " " + (10 * scale) + " 0 0 1 150 " + (150 + 10 * scale) + " Z");
}