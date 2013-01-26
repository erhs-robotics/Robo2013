include_directories(${CMAKE_CURRENT_BINARY_DIR})

add_executable(visionGUI gui.cpp)
target_link_libraries(visionGUI ${OpenCV_LIBS} vision)

add_executable(boardTest boardTest.cpp)
target_link_libraries(boardTest ${OpenCV_LIBS} vision)
