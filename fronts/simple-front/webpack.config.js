const path = require('path')
const HtmlWebPackPlugin = require('html-webpack-plugin')
const { CleanWebpackPlugin } = require('clean-webpack-plugin')
// var HtmlWebpackPlugin = require('html-webpack-plugin'); // https://github.com/jantimon/html-webpack-plugin
// var ExtractTextPlugin = require('extract-text-webpack-plugin'); // https://webpack.js.org/plugins/extract-text-webpack-plugin/

module.exports = {
    entry: './src/index.ts',
    devtool: 'inline-source-map',
    mode: 'development',
    watch: true,
    // resolve: {
    //     alias: {
    //         handlebars: 'handlebars/dist/handlebars.min.js'
    //     }
    // },
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                use: 'ts-loader',
                exclude: /node_modules/
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
                    // "handlebars-loader", // handlebars loader expects raw resource string
                    // "style-loader",
                    'extract-loader',
                    'css-loader'
                ]
            },
            // {
            //     test: /\.(png|svg|jpe?g|gif)$/,
            //     use: [
            //         {
            //             loader: 'file-loader',
            //             options: {
            //                 esModule: false
            //             }
            //         }
            //     ]
            // }
            {
                test: /\.hbs$/i,
                use: [

                    {
                        loader: 'handlebars-loader'
                    },
                    // {
                    //     loader: 'extract-loader'
                    // },
                    {
                        loader: 'html-loader'

                    }
                    // "extract-loader",
                ]
            }
        ]
    },
    plugins: [
        new CleanWebpackPlugin(),
        new HtmlWebPackPlugin({
            template: './src/index.html', // public/index.html 파일을 읽는다.
            filename: 'index.html' // output으로 출력할 파일은 index.html 이다.
        })
    ],
    resolve: {
        alias: {
            handlebars: __dirname + '/node_modules/handlebars/dist/handlebars.min.js',
            fs: false,
            '@src': path.resolve(__dirname, 'src')
        },
        extensions: ['.tsx', '.ts', '.js']
        // extensions: ['.tsx', '.ts', '.js', 'webpack.js', '.web.js', '.html'],
    },
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, 'dist')
    },
    devServer: {
        inline: true,
        hot: true,
        contentBase: __dirname + '/dist/',
        host: 'localhost',
        port: 5500,
        proxy: {
            '/': 'http://localhost:8080' // 프록시
        }
        // contentBase: './dist'
    }
}
