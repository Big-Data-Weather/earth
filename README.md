Quotaero - Senior Design Project
=====

** This Application is Merely  a Modification of the Null School Application Found @ https://github.com/cambecc/earth **

purpose
-------
The end goal is to compare model data and actual recorded data to find inconsistencies within our model.

building and launching
----------------------

After installing node.js and npm, clone "earth" and install dependencies:

    git clone https://github.com/cambecc/earth
    cd earth
    npm install

Next, launch the development web server:

    sudo node dev-server.js

Finally, point your browser to:

    http://localhost:80

The server acts as a stand-in for static S3 bucket hosting and so contains almost no server-side logic. It
serves all files located in the `earth/public` directory. See `public/index.html` and `public/libs/earth/*.js`
for the main entry points. Data files are located in the `public/data` directory, and there is one sample
weather layer located at `data/weather/current`.

*For Ubuntu, Mint, and elementary OS, use `nodejs` instead of `node` instead due to a [naming conflict](https://github.com/joyent/node/wiki/Installing-Node.js-via-package-manager#ubuntu-mint-elementary-os).

