// not sure what the stats thing is all about but not
// sure we need in a worker
function $stats(a) {
}

$strongName = '__STRONG_NAME__';


// there are some stray references to browser objects in the GWT
// code base that will not available to us. obviously we don't
// want to emulate the DOM but let's do all we can to get
// non-UI code to run.

navigator = { userAgent: 'gears' };
window = {};
window.google = google;
$wnd = window;
$doc = {};

gwtOnLoad(null, '__MODULE_NAME__', '');