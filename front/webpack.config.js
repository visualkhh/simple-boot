const path = require('path');
const HtmlWebPackPlugin = require("html-webpack-plugin");
// var HtmlWebpackPlugin = require('html-webpack-plugin'); // https://github.com/jantimon/html-webpack-plugin
// var ExtractTextPlugin = require('extract-text-webpack-plugin'); // https://webpack.js.org/plugins/extract-text-webpack-plugin/

module.exports = {
    entry: './src/index.ts',
    devtool: 'inline-source-map',
    mode: 'development',
    watch: true,
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                use: 'ts-loader',
                exclude: /node_modules/,
            },
            {
                test: /\.html$/i,
                loader: 'html-loader'
                // ,
                // options: {
                //     minimize: true
                // }
            },
            {
                test: /\.css$/i,
                use: [
                    "handlebars-loader", // handlebars loader expects raw resource string
                    "extract-loader",
                    "css-loader",
                ],
            },
            {
                test: /\.hbs$/i,
                use: [
                    "handlebars-loader", // handlebars loader expects raw resource string
                    // "extract-loader",
                    // "html-loader",
                ],
            },
        ],
    },
    plugins: [
        new HtmlWebPackPlugin({
            template: './src/index.html', // public/index.html 파일을 읽는다.
            filename: 'index.html' // output으로 출력할 파일은 index.html 이다.
        })
    ],
    resolve: {
        alias: {
            '@src': path.resolve(__dirname, 'src'),
        },
        extensions: ['.tsx', '.ts', '.js'],
        // extensions: ['.tsx', '.ts', '.js', 'webpack.js', '.web.js', '.html'],
    },
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, 'dist'),
    },
    devServer: {
        inline: true,
        hot: true,
        contentBase: __dirname + "/dist/",
        host: "localhost",
        port: 5500
        // contentBase: './dist'
    }
};
