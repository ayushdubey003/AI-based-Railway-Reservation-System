#include <boost/python/module.hpp>
#include <boost/python/def.hpp>
#include "test1.hh"
BOOST_PYTHON_MODULE(mymath)
{
  using namespace boost::python;
  def("add", add);
}
