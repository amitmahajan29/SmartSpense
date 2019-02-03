var content = [];
for(var i = 0; i < 30; i++) {
    content.push('Content line ' + i);
}

lines =[]; lines = Array.from(Array(content.length).keys()).map((n) => content[n]);  

var fs = require('fs');
var PDFDocument = require('pdfkit');

function printLinesToPDF() {

    return new Promise((resolve) => {

        var pdf = new PDFDocument({
          size: 'A4', 
          info: {
            Title: 'Example file',
            Author: 'Me',
          }
        });

        console.log('Printing %d line(s) to PDF', lines.length);
        lines.forEach((line) => pdf.text(line));

        var stream = fs.createWriteStream('./example.pdf');
        pdf.pipe(stream);

        pdf.flushPages();
        pdf.end();

        stream.on('finish', function() {
            resolve();
        });

    });
}

printLinesToPDF().then(() => {
    console.log('All done');
});