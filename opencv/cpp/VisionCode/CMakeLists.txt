cmake_minimum_required(VERSION 2.8)
project(visioncode)
find_package(OpenCV REQUIRED)

include_directories({CMAKE_CURRENT_BINARY_DIR} lib src)
add_subdirectory(lib)
add_subdirectory(src bin)



