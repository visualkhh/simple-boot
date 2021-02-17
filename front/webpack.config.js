const path = require('path');
const HtmlWebpackPlugin = require("html-webpack-plugin");
// var HtmlWebpackPlugin = require('html-webpack-plugin'); // https://github.com/jantimon/html-webpack-plugin
// var ExtractTextPlugin = require('extract-text-webpack-plugin'); // https://webpack.js.org/plugins/extract-text-webpack-plugin/

module.exports = {
    entry: './src/index.ts',
    devtool: 'inline-source-map',
    watch: true,
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                use: 'ts-loader',
                exclude: /node_modules/,
            },
        ],
    },
    plugins: [
        new HtmlWebpackPlugin()
    ],
    resolve: {
        alias: {
            '@src': path.resolve(__dirname, 'src'),
        },
        extensions: [ '.tsx', '.ts', '.js' ],
    },
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, 'dist'),
    },
    devServer: {
        inline: true,
        hot:true,
        contentBase: __dirname + "/dist/",
        host: "localhost",
        port: 5500
        // contentBase: './dist'
    }
};
