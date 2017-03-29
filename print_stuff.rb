
def print_while_sleeping(num_of_times, counter)
  until counter > num_of_times
    puts "********************************"
    puts "sleeping"
    puts "..."
    puts "counter: #{counter}"
    puts "..."
    puts "num_of_times: #{num_of_times}"
    puts "********************************"
    counter += 1
    num_of_times -= 1
    print_while_sleeping(num_of_times, counter)
  end
end

print_while_sleeping(20, 0)
